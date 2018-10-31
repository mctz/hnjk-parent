package com.hnjk.edu.teaching.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.teaching.model.UsualResultsHistory;
import com.hnjk.edu.teaching.service.IUsualResultsHistoryService;


@Transactional
@Service("usualResultsHistoryService")
public class UsualResultsHistoryServiceImpl extends BaseServiceImpl<UsualResultsHistory> implements IUsualResultsHistoryService {

}
