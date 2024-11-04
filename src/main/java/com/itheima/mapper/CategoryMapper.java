package com.itheima.mapper;

import com.itheima.pojo.Category;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CategoryMapper {
    @Insert("insert into category(category_name,category_alias,created_time,updated_time) values(#{categoryName},#{categoryAlias},now(),now())")
    void add(Category category);

    @Select("select * from category where create_user = #{userid}")
    List<Category> list(Integer userid);

    @Select("select * from category where id = #{id}")
    Category detail(Integer id);

    @Update("update category set category_name = #{categoryName}, category_alias = #{categoryAlias} ,updated_time = now() where id = #{id}")
    void update(Category category);
}
