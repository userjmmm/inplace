import styled from 'styled-components';
import { PiHeartFill, PiHeartLight } from 'react-icons/pi';
import { useCallback, useEffect, useRef, useState } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { useLocation } from 'react-router-dom';
import { Text } from '../common/typography/Text';
import FallbackImage from '../common/Items/FallbackImage';
import { Paragraph } from '../common/typography/Paragraph';
import { CommentData } from '@/types';
import { useDeleteComment } from '@/api/hooks/useDeleteComment';
import useAuth from '@/hooks/useAuth';
import { usePostCommentLike } from '@/api/hooks/usePostCommentLike';
import LoginModal from '../common/modals/LoginModal';
import { usePutComment } from '@/api/hooks/usePutComment';
import Button from '../common/Button';
import useAutoResizeTextarea from '@/hooks/Post/useAutoResizeTextarea';
import useTheme from '@/hooks/useTheme';
import EditMenu from './EditMenu';
import UserName from './UserName';
import useIsMobile from '@/hooks/useIsMobile';
import ReportModal from '../common/modals/ReportModal';
import { usePostReportComment } from '@/api/hooks/usePostReportComment';

export default function CommentItem({
  item,
  postId,
  currentPage,
}: {
  item: CommentData;
  postId: string;
  currentPage: number;
}) {
  const { isAuthenticated } = useAuth();
  const location = useLocation();
  const queryClient = useQueryClient();
  const { theme } = useTheme();
  const isDarkMode = theme === 'dark';
  const isMobile = useIsMobile();

  const [showLoginModal, setShowLoginModal] = useState(false);
  const [isLike, setIsLike] = useState(item.selfLike);
  const [isReportModalOpen, setIsReportModalOpen] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [editValue, setEditValue] = useState(item.content);
  const [reportStatus, setReportStatus] = useState<'idle' | 'success' | 'error'>('idle');
  const textareaRef = useRef<HTMLTextAreaElement>(null);

  const { mutate: deleteComment } = useDeleteComment();
  const { mutate: putComment } = usePutComment();
  const { mutate: postLike } = usePostCommentLike();
  const { mutate: reportComment } = usePostReportComment();

  const handleResizeHeight = useAutoResizeTextarea();

  function highlightMentions(text: string) {
    const regex = /<<@(\d+):([^>]+)>>/g;
    const parts = [];
    let lastIndex = 0;
    let match = regex.exec(text);
    while (match !== null) {
      if (match.index > lastIndex) {
        parts.push(text.slice(lastIndex, match.index));
      }
      parts.push(
        <span key={match[1]} style={{ color: '#00c4c4', fontWeight: 700 }}>
          @{match[2]}
        </span>,
      );
      lastIndex = regex.lastIndex;
      match = regex.exec(text);
    }
    // 남은 텍스트
    if (lastIndex < text.length) {
      parts.push(text.slice(lastIndex));
    }
    return parts;
  }

  const handleDeleteSubmit = () => {
    const isConfirm = window.confirm('삭제하시겠습니까?');
    if (!isConfirm) return;

    deleteComment(
      { postId, id: item.commentId.toString() },
      {
        onSuccess: () => {
          alert('삭제되었습니다.');
          queryClient.invalidateQueries({ queryKey: ['commentList', postId, currentPage - 1, 10] });
          queryClient.invalidateQueries({ queryKey: ['postData', postId] }); // 댓글 건수 갱신
        },
        onError: () => {
          alert('댓글 삭제에 실패했어요. 다시 시도해주세요!');
        },
      },
    );
  };

  const handleEditClick = () => {
    setIsEditing(true);
    setEditValue(item.content);
  };

  const handleEditSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (editValue.trim() === item.content.trim()) {
      setIsEditing(false);
      return;
    }
    if (!editValue.trim()) {
      alert('댓글을 입력해주세요.');
      return;
    }

    putComment(
      { postId, commentId: item.commentId.toString(), comment: editValue },
      {
        onSuccess: () => {
          queryClient.invalidateQueries({ queryKey: ['commentList', postId, currentPage - 1, 10] });
          setIsEditing(false);
        },
        onError: () => {
          alert('댓글 수정에 실패했어요. 다시 시도해주세요!');
        },
      },
    );
  };

  const handleReportSubmit = async (type: string, content: string) => {
    const reason = content ? `${type} : ${content}` : type;

    try {
      await new Promise<void>((resolve, reject) => {
        reportComment(
          { id: Number(item.commentId), reason },
          {
            onSuccess: () => {
              setReportStatus('success');
              resolve();
            },
            onError: (err) => {
              setReportStatus('error');
              reject(err);
            },
          },
        );
      });
    } catch (e) {
      console.error('신고 실패');
    }
  };
  const handleReportClose = () => {
    setIsReportModalOpen(false);
    setReportStatus('idle');
  };

  const handleLikeClick = useCallback(
    (event: React.MouseEvent<HTMLDivElement>) => {
      event.stopPropagation();
      event.preventDefault();
      if (!isAuthenticated) {
        setShowLoginModal(true);
        return;
      }
      const newLikeStatus = !isLike;
      postLike(
        { postId, commentId: Number(item.commentId), likes: newLikeStatus },
        {
          onSuccess: () => {
            setIsLike(newLikeStatus);
            queryClient.invalidateQueries({ queryKey: ['commentList', postId, currentPage - 1, 10] });
          },
          onError: () => {
            alert('좋아요 등록에 실패했어요. 다시 시도해주세요!');
          },
        },
      );
    },
    [isLike, item.commentId, postLike],
  );

  useEffect(() => {
    if (isEditing && textareaRef.current) {
      textareaRef.current.focus();
      const len = textareaRef.current.value.length;
      textareaRef.current.setSelectionRange(len, len);
      handleResizeHeight(textareaRef.current);
    }
  }, [isEditing, handleResizeHeight]);

  return (
    <Wrapper data-comment-id={item.commentId}>
      <CommentTop>
        <UserInfo>
          <ProfileImg>
            <FallbackImage src={item.author.imgUrl} alt="profile" />
          </ProfileImg>
          <UserName userNickname={item.author.nickname} tierImageUrl={item.author.tierImageUrl} />
        </UserInfo>
        {!isEditing && (
          <EditMenu
            mine={item.isMine}
            onEdit={handleEditClick}
            onDelete={handleDeleteSubmit}
            onReport={() => setIsReportModalOpen(true)}
            ariaLabels="댓글"
          />
        )}
      </CommentTop>
      <Content>
        {isEditing ? (
          <form onSubmit={handleEditSubmit} style={{ width: '100%' }}>
            <TextArea
              ref={textareaRef}
              value={editValue}
              onChange={(e) => {
                setEditValue(e.target.value);
                handleResizeHeight(e.target);
              }}
              rows={1}
            />
            <SubmitBtnWrapper>
              <StyledButton
                type="button"
                variant={isDarkMode ? 'outline' : 'blackOutline'}
                size="small"
                aria-label="댓글 수정 취소"
                onClick={() => {
                  setIsEditing(false);
                  setEditValue(item.content);
                }}
              >
                취소
              </StyledButton>
              <StyledButton
                type="submit"
                aria-label="댓글 수정 완료"
                variant={isDarkMode ? 'white' : 'black'}
                size="small"
              >
                완료
              </StyledButton>
            </SubmitBtnWrapper>
          </form>
        ) : (
          <>
            <Paragraph size="s" weight="normal">
              {highlightMentions(item.content)}
            </Paragraph>
            <CommentInfo>
              <StyledText size="s" weight="normal">
                {item.createdAt}
              </StyledText>
              <Count
                role="button"
                aria-label="댓글 좋아요 버튼"
                onClick={(e: React.MouseEvent<HTMLDivElement>) => handleLikeClick(e)}
              >
                {isLike ? (
                  <PiHeartFill color="#fe7373" size={isMobile ? 14 : 18} data-testid="PiHeartFill" />
                ) : (
                  <PiHeartLight size={isMobile ? 14 : 18} data-testid="PiHeartLight" />
                )}
                <StyledText size="s" weight="normal">
                  {item.totalLikeCount ?? 0}
                </StyledText>
              </Count>
            </CommentInfo>
          </>
        )}
      </Content>
      {showLoginModal && (
        <LoginModal immediateOpen currentPath={location.pathname} onClose={() => setShowLoginModal(false)} />
      )}
      {isReportModalOpen && (
        <ReportModal onClose={handleReportClose} onSubmit={handleReportSubmit} status={reportStatus} />
      )}
    </Wrapper>
  );
}

const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  padding: 14px;
  border-radius: 16px;
  background: none;
  border: none;
  justify-content: space-between;
  gap: 8px;

  @media screen and (max-width: 768px) {
    padding: 8px 0px;
    gap: 0px;
  }
`;
const Content = styled.div`
  width: calc(100%-40px);
  display: flex;
  white-space: pre-line;
  flex-direction: column;
  padding-left: 40px;
  gap: 8px;
  p {
    line-height: 150%;
  }
`;
const CommentTop = styled.div`
  width: 100%;
  display: flex;
  justify-content: space-between;
`;
const CommentInfo = styled.div`
  display: flex;
  gap: 8px;
  align-items: center;
`;
const UserInfo = styled.div`
  display: flex;
  gap: 8px;
  align-items: center;
  @media screen and (max-width: 768px) {
    gap: 4px;
  }
`;
const ProfileImg = styled.div`
  height: 34px;
  aspect-ratio: 1 / 1;
  border-radius: 50%;
  @media screen and (max-width: 768px) {
    height: 30px;
  }
`;

const StyledText = styled(Text)`
  line-height: 120%;
  white-space: pre-line;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#bbbbbb' : '#717171')};
`;

const Count = styled.div`
  display: flex;
  align-items: flex-start;
  gap: 4px;
  cursor: pointer;
  svg {
    color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#bbbbbb' : '#717171')};
  }
  span {
    line-height: 120%;
  }
  @media screen and (max-width: 768px) {
    gap: 2px;
    align-items: center;
  }
`;

const TextArea = styled.textarea`
  width: 100%;
  padding: 10px;
  box-sizing: border-box;
  font-size: 14px;
  line-height: 1.4;
  border: 1px solid #c9c9c9;
  border-radius: 10px;
  background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#1f1f1f' : '#eefbfb')};
  overflow-y: hidden;
  resize: none;
  color: ${({ theme }) => (theme.backgroundColor === '#292929' ? 'white' : 'black')};
  margin-bottom: 4px;
  &:focus {
    outline-color: #00c4c4;
  }
`;

const StyledButton = styled(Button)`
  width: 60px;
  font-size: 14px;
  height: 32px;
  cursor: pointer;
`;

const SubmitBtnWrapper = styled.div`
  margin-top: 6px;
  display: flex;
  width: 100%;
  gap: 10px;
  justify-content: flex-end;
`;
