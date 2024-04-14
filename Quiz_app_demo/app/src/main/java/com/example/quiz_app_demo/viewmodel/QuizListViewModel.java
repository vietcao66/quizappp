package com.example.quiz_app_demo.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.quiz_app_demo.model.QuizListModel;
import com.example.quiz_app_demo.repository.QuizListRepository;

import java.util.List;

public class QuizListViewModel extends ViewModel implements QuizListRepository.onFirestoneTaskComplete {
    private MutableLiveData<List<QuizListModel>> quizListLiveData=new MutableLiveData<List<QuizListModel>>();
    private QuizListRepository repository=new QuizListRepository(this);

    public MutableLiveData<List<QuizListModel>> getQuizListLiveData() {
        return quizListLiveData;
    }

    public QuizListViewModel(){
        repository.getQuizData();
    }
    @Override
    public void quizDataLoad(List<QuizListModel> quizListModels) {
        quizListLiveData.setValue(quizListModels);
    }

    @Override
    public void onError(Exception e) {
        Log.d("QuizERROR", "onError: " + e.getMessage());
    }
}
