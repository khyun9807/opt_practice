package com.optpractice.post.repository;

import com.optpractice.post.entity.Post;
import com.optpractice.post.entity.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndIsDeletedFalse(Long id);

    boolean existsByIdAndIsDeletedFalse(Long id);

    @Modifying
    @Query("""
        update Post p
        set p.commentCount = case
            when p.commentCount < :count then 0
            else p.commentCount - :count
        end
        where p.id = :postId
    """)
    void decreaseCommentCount(@Param("postId") Long postId, @Param("count") long count);

    @Query(
        value = """
          select p from Post p
          join fetch p.author
          join fetch p.merchant
          where p.isDeleted = false
        """,
        countQuery = """
          select count(p) from Post p
          where p.isDeleted = false
        """
    )
    Page<Post> findActivePosts(
            Pageable pageable
    );

    @Query(
            value = """
          select p from Post p
          join fetch p.author
          join fetch p.merchant
          where p.isDeleted = false
            and (p.merchant.id = :merchantId)
        """,
            countQuery = """
          select count(p) from Post p
          where p.isDeleted = false
            and (p.merchant.id = :merchantId)
        """
    )
    Page<Post> findActivePostsByMerchant(
            @Param("merchantId") Long merchantId,
            Pageable pageable
    );

    @Query(
            value = """
          select p from Post p
          join fetch p.author
          join fetch p.merchant
          where p.isDeleted = false
            and (p.type = :type)
        """,
            countQuery = """
          select count(p) from Post p
          where p.isDeleted = false
            and (p.type = :type)
        """
    )
    Page<Post> findActivePostsByType(
            @Param("type") PostType type,
            Pageable pageable
    );

    @Query(
            value = """
          select p from Post p
          join fetch p.author
          join fetch p.merchant
          where p.isDeleted = false
            and (p.merchant.id = :merchantId)
            and (p.type = :type)
        """,
            countQuery = """
          select count(p) from Post p
          where p.isDeleted = false
            and (p.merchant.id = :merchantId)
            and (p.type = :type)
        """
    )
    Page<Post> findActivePostsByMerchantAndType(
            @Param("merchantId") Long merchantId,
            @Param("type") PostType type,
            Pageable pageable
    );
}