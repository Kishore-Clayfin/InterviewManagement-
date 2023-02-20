package com.cf.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cf.model.History;
import com.cf.repository.IHistoryDao;
@Service
public class HistoryService implements IHistoryService{

	@Autowired
	private IHistoryDao iHistoryDao;
	@Override
	public History saveHistory(History history) {
		// TODO Auto-generated method stub
		History historyAfterSave=iHistoryDao.save(history);
		
		return historyAfterSave;
	}

	@Override
	public void deleteHistory(Integer historyId) {
		// TODO Auto-generated method stub
		iHistoryDao.deleteById(historyId);
	}

	@Override
	public History updateHistory(Integer historyId) {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public History findHistoryById(Integer historyId) {
		// TODO Auto-generated method stub
		return iHistoryDao.findById(historyId).orElseThrow();
	}

	@Override
	public List<History> findAllHistory() {
		// TODO Auto-generated method stub
		List<History> ListOfHistory=iHistoryDao.findAll();
		return ListOfHistory;
	}

}