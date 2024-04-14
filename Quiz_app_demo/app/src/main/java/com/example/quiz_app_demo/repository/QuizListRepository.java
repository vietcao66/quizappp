package com.example.quiz_app_demo.repository;

import androidx.annotation.NonNull;

import com.example.quiz_app_demo.model.QuizListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class QuizListRepository {
    private onFirestoneTaskComplete onFirestoneTaskComplete;
    private FirebaseFirestore firebaseFirestore =FirebaseFirestore.getInstance();
    private CollectionReference reference= firebaseFirestore.collection("Quiz");


    public QuizListRepository(onFirestoneTaskComplete onFirestoneTaskComplete){
        this.onFirestoneTaskComplete=onFirestoneTaskComplete;
    }
    public void getQuizData(){
        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    onFirestoneTaskComplete.quizDataLoad(task.getResult().toObjects(QuizListModel.class));
                }else{
                    onFirestoneTaskComplete.onError(task.getException());
                }
            }
        });
    }

    public interface onFirestoneTaskComplete{
        void quizDataLoad(List<QuizListModel> quizListModelList);
        void onError(Exception e);
    }
}
