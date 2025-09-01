import styled from 'styled-components';
import { useEffect, useRef, useState } from 'react';
import { IoIosSend } from 'react-icons/io';
import { useQueryClient } from '@tanstack/react-query';
import { useLocation } from 'react-router-dom';
import CommentItem from './CommentItem';
import { useGetCommentList } from '@/api/hooks/useGetCommentList';
import { usePostComment } from '@/api/hooks/usePostComment';
import useAuth from '@/hooks/useAuth';
import LoginModal from '../common/modals/LoginModal';
import FallbackImage from '../common/Items/FallbackImage';
import { useGetUserInfo } from '@/api/hooks/useGetUserInfo';
import { Text } from '../common/typography/Text';
import useAutoResizeTextarea from '@/hooks/Post/useAutoResizeTextarea';
import Pagination from '../common/Pagination';
import Loading from '../common/layouts/Loading';
import NoItem from '../common/layouts/NoItem';
import { useGetSearchUserComplete } from '@/api/hooks/useGetSearchUserComplete';
import useDebounce from '@/hooks/useDebounce';
import UserName from './UserName';
import useClickOutside from '@/hooks/useClickOutside';
import useMentionableUsers from '@/hooks/Comment/useMentionableUsers';
import { convertMentionsToEntities, extractMentionQuery } from '@/libs/mention/metion';

export default function Comment({ id }: { id: string }) {
  const location = useLocation();
  const [showLoginModal, setShowLoginModal] = useState(false);
  const [showMentionList, setShowMentionList] = useState(false);
  const [inputValue, setInputValue] = useState('');
  const [mentionQuery, setMentionQuery] = useState('');
  const textareaRef = useRef<HTMLTextAreaElement>(null);
  const mentionListRef = useRef<HTMLDivElement>(null);

  const searchParams = new URLSearchParams(location.search);
  const commentPage = searchParams.get('commentPage');
  const commentId = searchParams.get('commentId');
  const [currentPage, setCurrentPage] = useState(commentPage ? Number(commentPage) + 1 : 1);

  const queryClient = useQueryClient();
  const { isAuthenticated } = useAuth();
  const { data: userInfo } = useGetUserInfo();
  const { mutate: postComment } = usePostComment();
  const { data: commentList, isLoading } = useGetCommentList(id, currentPage - 1, 10);

  const debouncedMentionQuery = useDebounce(mentionQuery, 300);
  const { data: searchResults } = useGetSearchUserComplete(id, debouncedMentionQuery, !!debouncedMentionQuery);

  const handleResizeHeight = useAutoResizeTextarea();

  const mentionableUsers = useMentionableUsers(searchResults);

  useEffect(() => {
    // 같은 post라도 commentPage가 다를 경우 해당 페이지로 변경
    const newPage = commentPage ? Number(commentPage) + 1 : 1;
    setCurrentPage(newPage);
  }, [commentPage]);

  useEffect(() => {
    if (commentId && commentList) {
      const idToScroll = Number(commentId);
      // 댓글 위치 찾기 위해, 댓글 ID에 해당하는 DOM 요소를 브라우저에서 찾아서 js객체로 가져옴
      const targetComment = document.querySelector(`[data-comment-id="${idToScroll}"]`) as HTMLElement;
      if (targetComment) {
        targetComment.scrollIntoView({ behavior: 'smooth', block: 'center' });
        // 스크롤 이동 후 URL에서 쿼리 파라미터를 제거해서 새로고침 시 이동하지 않도록 처리
        const newUrl = `${location.pathname}`;
        window.history.replaceState({}, '', newUrl);
      }
    }
  }, [commentList, commentId, location.pathname]);

  const handleFocus = (e: React.FocusEvent<HTMLTextAreaElement>) => {
    if (!isAuthenticated) {
      setShowLoginModal(true);
      if (textareaRef.current) {
        textareaRef.current.blur();
      }
    }
    const { value } = e.target;
    const cursorPos = e.target.selectionStart;
    const query = extractMentionQuery(value, cursorPos);
    if (query !== '' || value.includes('@')) {
      setShowMentionList(true);
      setMentionQuery(query);
    }
  };

  // 텍스트 변경 시 닉네임 추출
  const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    const { value } = e.target;
    setInputValue(value);

    const atIndex = value.lastIndexOf('@');
    if (atIndex !== -1) {
      // @ 뒤에 공백이나 줄바꿈이 오면 mentionQuery를 비움
      const afterAt = value.slice(atIndex + 1);
      if (afterAt === '' || /^[^\s]+$/.test(afterAt)) {
        setShowMentionList(true);
        setMentionQuery(afterAt);
      } else {
        setShowMentionList(false);
        setMentionQuery('');
      }
    } else {
      setShowMentionList(false);
      setMentionQuery('');
    }
  };

  const handleSelectUser = (nickname: string) => {
    if (!textareaRef.current) return;
    const { value } = textareaRef.current;
    const cursorPos = textareaRef.current.selectionStart;
    const beforeCursor = value.slice(0, cursorPos);
    const atIndex = beforeCursor.lastIndexOf('@');
    if (atIndex === -1) return;
    textareaRef.current.focus();

    // @에서 커서까지를 @닉네임으로 대체
    const newBefore = `${beforeCursor.slice(0, atIndex)}@${nickname} `;
    const newValue = newBefore + value.slice(cursorPos);
    setInputValue(newValue);
    setShowMentionList(false);
    setMentionQuery('');
    handleResizeHeight(textareaRef.current);
  };

  const handleCommentSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!textareaRef.current || inputValue === '') return;

    const convertedValue = convertMentionsToEntities(inputValue, mentionableUsers);

    postComment(
      { postId: id, comment: convertedValue },
      {
        onSuccess: () => {
          if (!textareaRef.current) return;
          setInputValue('');
          queryClient.invalidateQueries({ queryKey: ['commentList', id, currentPage - 1, 10] }); // 댓글 리스트 초기화
          queryClient.invalidateQueries({ queryKey: ['postData', id] }); // 댓글 건수 갱신
          textareaRef.current.style.height = 'auto';
          setCurrentPage(commentList?.totalPages ?? 1);
        },
        onError: () => {
          alert('댓글 등록에 실패했어요. 다시 시도해주세요!');
        },
      },
    );
  };
  const handlePageChange = (pageNum: number) => {
    setCurrentPage(pageNum);
  };

  useClickOutside([mentionListRef, textareaRef], () => setShowMentionList(false));

  const placeholder = isAuthenticated ? '의견을 남겨주세요.' : '댓글을 작성하려면 로그인이 필요해요.';

  if (isLoading) {
    return (
      <Wrapper>
        <LoadingWrapper>
          <Loading size={30} />
        </LoadingWrapper>
      </Wrapper>
    );
  }

  return (
    <>
      <Wrapper>
        {!commentList || commentList.content.length === 0 ? (
          <NoItem message="댓글이 없습니다." alignItems="center" />
        ) : (
          <>
            {commentList.content.map((item) => (
              <CommentItem key={item.commentId} item={item} postId={id} currentPage={currentPage} />
            ))}
            <Pagination
              currentPage={currentPage}
              totalPages={commentList.totalPages}
              totalItems={commentList.totalElements}
              onPageChange={handlePageChange}
              itemsPerPage={commentList.pageable.pageSize}
            />
          </>
        )}
        <CommentContainer>
          <UserInfo>
            <ProfileImg>
              <FallbackImage src={isAuthenticated ? userInfo?.imgUrl : ''} alt="profile" />
            </ProfileImg>
            <Text size="s" weight="normal">
              {isAuthenticated ? userInfo?.nickname : `사용자`}
            </Text>
          </UserInfo>
          <Content>
            <CommentInputWrapper onSubmit={handleCommentSubmit}>
              {showMentionList && searchResults && searchResults?.length > 0 && (
                <MentionList ref={mentionListRef}>
                  {searchResults?.map((user) => (
                    <MentionItem key={user.userId} onClick={() => handleSelectUser(user.nickname)}>
                      <UserInfo>
                        <ProfileImg>
                          <FallbackImage src={user.imageUrl} alt={`${user.userId} profile`} />
                        </ProfileImg>
                        <UserName userNickname={user.nickname} />
                      </UserInfo>
                    </MentionItem>
                  ))}
                </MentionList>
              )}
              <TextArea
                ref={textareaRef}
                value={inputValue}
                placeholder={placeholder}
                rows={1}
                onFocus={handleFocus}
                onChange={handleChange}
              />
              <SendButton type="submit">
                <IoIosSend size={20} />
              </SendButton>
            </CommentInputWrapper>
          </Content>
        </CommentContainer>
      </Wrapper>
      {showLoginModal && (
        <LoginModal immediateOpen currentPath={location.pathname} onClose={() => setShowLoginModal(false)} />
      )}
    </>
  );
}

const Wrapper = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 8px;
`;
const CommentInputWrapper = styled.form`
  position: relative;
`;
const TextArea = styled.textarea`
  width: 100%;
  padding: 10px 50px 10px 10px;
  box-sizing: border-box;
  font-size: 14px;
  line-height: 1.4;
  display: flex;
  border: 1px solid #c9c9c9;
  border-radius: 10px;
  background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#1f1f1f' : '#eefbfb')};
  overflow-y: hidden;
  resize: none;
  color: ${({ theme }) => (theme.backgroundColor === '#292929' ? 'white' : 'black')};

  &::placeholder {
    color: #8b8b8b;
  }

  &:focus {
    outline-color: #00c4c4;
  }
`;

const SendButton = styled.button`
  position: absolute;
  right: 10px;
  top: 50%;
  display: flex;
  transform: translateY(-50%);
  background: transparent;
  border: none;

  color: ${({ theme }) => (theme.backgroundColor === '#292929' ? 'white' : 'black')};
  cursor: pointer;
`;

const CommentContainer = styled.div`
  display: flex;
  width: 100%;
  gap: 14px;
  padding: 14px;
  flex-direction: column;
  box-sizing: border-box;
`;
const Content = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  text-align: start;
  gap: 14px;
  p {
    line-height: 150%;
  }
`;
const UserInfo = styled.div`
  display: flex;
  gap: 8px;
  align-items: center;
`;
const ProfileImg = styled.div`
  height: 34px;
  aspect-ratio: 1 / 1;
  border-radius: 50%;
`;

const LoadingWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 60px;
  margin-top: 20px;
`;

const MentionList = styled.div`
  position: absolute;
  bottom: 100%;
  left: 0;
  width: 100%;
  max-height: 200px;
  overflow-y: auto;
  background: ${({ theme }) => theme.backgroundColor};
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 2px;
  box-sizing: border-box;
  z-index: 10;

  &::-webkit-scrollbar {
    width: 6px;
    background: transparent;
  }
  &::-webkit-scrollbar-thumb {
    background: #6c6c6c;
    border-radius: 8px;
  }
`;

const MentionItem = styled.div`
  padding: 10px 12px;
  cursor: pointer;
  &:hover {
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#1f1f1f' : '#eefbfb')};
  }
`;
