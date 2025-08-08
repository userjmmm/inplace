package post.query;

import annotation.Facade;
import base.CursorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import post.query.dto.PostResult;

import java.util.List;

@Facade
@RequiredArgsConstructor
public class PostQueryFacade {
    
    private final PostQueryService postQueryService;
    
    public CursorResult<PostQueryResult.DetailedPost> getPosts(Long cursorId, int size, String orderBy) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        
        return postQueryService.getPosts(userId, cursorId, size, orderBy);
    }
    
    public PostQueryResult.DetailedPost getPostById(Long postId) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        return postQueryService.getPostById(postId, userId);
    }
    
    public PostResult.PostImages getPostImageDetails(Long postId) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        return postQueryService.getPostImageDetails(postId, userId);
    }
    
    public Page<CommentQueryResult.DetailedComment> getCommentsByPostId(
        Long postId, Pageable pageable
    ) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        return postQueryService.getCommentsByPostId(postId, userId, pageable);
    }
    
    public List<PostQueryResult.UserSuggestion> getCommentUserSuggestions(Long postId, String keyword) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        return postQueryService.getUserSuggestions(userId, postId, keyword);
    }
}
