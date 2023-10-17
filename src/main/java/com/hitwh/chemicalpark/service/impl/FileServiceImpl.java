package com.hitwh.chemicalpark.service.impl;

import com.hitwh.chemicalpark.entity.Files;
import com.hitwh.chemicalpark.mapper.FileMapper;
import com.hitwh.chemicalpark.service.IFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author PJK
 * @since 2023-04-11
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, Files> implements IFileService {

}
