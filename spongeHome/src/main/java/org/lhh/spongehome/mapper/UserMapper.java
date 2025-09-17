package org.lhh.spongehome.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.lhh.spongehome.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mybatis.spring.annotation.MapperScan;

/**
* @author 19589
* @description 针对表【user】的数据库操作Mapper
* @createDate 2025-05-31 23:54:40
* @Entity org.lhh.spongehome.model.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {


}




