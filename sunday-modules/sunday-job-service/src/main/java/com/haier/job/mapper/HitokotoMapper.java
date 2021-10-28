package com.haier.job.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haier.job.domain.Hitokoto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Entity generator.domain.Hitokoto
 */
@Mapper
public interface HitokotoMapper extends BaseMapper<Hitokoto> {

    int batchInsertHitokotos(List<Hitokoto> hitokotos);
}




