package com.study.repository.jdbc;

import com.study.domain.cnu.CnuComment;
import com.study.domain.cnu.CnuPost;
import com.study.domain.cnu.CnuPostComment;
import com.study.repository.mybatis.CnuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by rokim on 2016. 5. 22..
 */
@Repository(value = "cnuJdbcRepository")
public class CnuJdbcRepository {
    @Autowired private JdbcTemplate jdbcTemplate;

    private final RowMapper<CnuPost> cnuPostMapper = new RowMapper<CnuPost>() {
        @Override
        public CnuPost mapRow(ResultSet resultSet, int i) throws SQLException {
            CnuPost cnuPost = new CnuPost();
            cnuPost.setPostId(resultSet.getInt("post_id"));
            cnuPost.setTitle(resultSet.getString("title"));
            cnuPost.setViewCount(resultSet.getInt("view_count"));

            return cnuPost;
        }
    };

    private final RowMapper<CnuComment> cnuCommentMapper = new RowMapper<CnuComment>() {
        @Override
        public CnuComment mapRow(ResultSet resultSet, int i) throws SQLException {
            CnuComment cnuComment = new CnuComment();
            cnuComment.setCommentId(resultSet.getInt("comment_id"));
            cnuComment.setAuthor(resultSet.getString("author"));
            cnuComment.setDel(resultSet.getBoolean("is_del"));
            cnuComment.setComment(resultSet.getString("comment"));
            cnuComment.setCreateTime(resultSet.getDate("create_time"));
            cnuComment.setPostId(resultSet.getInt("post_id"));
            cnuComment.setPassword(resultSet.getString("password"));
            return cnuComment;
        }
    };

    public List<CnuPost> selectCnuPostList() {
        return jdbcTemplate.query("SELECT * FROM cnu_post", cnuPostMapper);
    }

    public List<CnuComment> selectCnuCommentList(int postId) {
        return jdbcTemplate.query("SELECT * FROM cnu_post_comment WHERE post_id = ?", cnuCommentMapper, postId);
    }


    public CnuPost selectCnuPost(int postId) {
        return jdbcTemplate.queryForObject("SELECT * FROM cnu_post WHERE post_id = ?", cnuPostMapper, postId);
    }

    
    //어떻게 하나의 트렌젝션으로 묶을까?  => AOP 
    @Transactional   //이 코드 한줄의 효과 : 이안에서 exception이 생기면 커밋을 안해  
    public int increaseViewCount(CnuPost obj) {
   	
    	jdbcTemplate.update("UPDATE cnu_post SET view_count = view_count + 1 WHERE post_id = ?", obj.getPostId());
    	if(true){
    		//IDE가 너무 똑똑해서 
    		throw new RuntimeException();
    	}
    	return 0 ;
    }
//@retry(value = 3) 네트워크는 신뢰할 수 없어 그 때마다 error를 뿝는게 아니라 한번 해보고   --> 왜 만들었나?
//애러가 났을 때 ? 
    
}
