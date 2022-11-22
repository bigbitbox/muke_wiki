package com.rrzui.wiki.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrzui.wiki.entity.Doc;
import com.rrzui.wiki.exception.BusinessException;
import com.rrzui.wiki.exception.BusinessExceptionCode;
import com.rrzui.wiki.mapper.DocMapper;
import com.rrzui.wiki.service.DocService;
import com.rrzui.wiki.utils.RedisUtil;
import com.rrzui.wiki.utils.RequestContext;
import com.rrzui.wiki.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: 粤嵌项目
 * @description:
 * @author: Ethercat
 * @create: 2022-11-15 09:38
 **/

@Service
public class DocServiceImpl extends ServiceImpl<DocMapper, Doc> implements DocService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private WebSocketServer webSocketServer;
    @Override
    public int increaseViewCount(int id) {
        return this.baseMapper.increaseViewCount(id);
    }

    @Override
    public int increaseVoteCount(int id) {
        //远程ip+doc.id 24小时不能重复点赞
        String ip = RequestContext.getRemoteAddr();
        Doc docDB = this.baseMapper.selectById(id);
        if (redisUtil.validateRepeat("DOC_VOTE_"+id+"_"+ip,3600*24)) {
            webSocketServer.sendInfo("["+docDB.getName()+"]被点赞了");
            return this.baseMapper.increaseVoteCount(id);
        } else {
            webSocketServer.sendInfo("["+docDB.getName()+"]这篇文章你已经点过赞了");
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }
    }

    @Override
    public int updateEbookInfo() {
        return this.baseMapper.updateEbookInfo();
    }


}