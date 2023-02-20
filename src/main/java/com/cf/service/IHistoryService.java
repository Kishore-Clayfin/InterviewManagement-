package com.cf.service;
import java.util.List;

import com.cf.model.History;

public interface IHistoryService {
public History saveHistory(History history);
public void deleteHistory(Integer historyId);
public History updateHistory(Integer historyId);
public History findHistoryById(Integer historyId);
public List<History> findAllHistory();
}
