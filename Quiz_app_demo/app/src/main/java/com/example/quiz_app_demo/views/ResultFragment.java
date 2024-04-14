package com.example.quiz_app_demo.views;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quiz_app_demo.R;
import com.example.quiz_app_demo.viewmodel.QuestionViewModel;

import java.util.HashMap;


public class ResultFragment extends Fragment {

    private NavController navController;
    private QuestionViewModel viewModel;
    private TextView correctAnswer , wrongAnswer , notAnswered;
    private TextView percentTv,judgement;
    private ProgressBar scoreProgressbar;
    private String quizId;
    private Button homeBtn;
    private Handler handler;
    private Runnable runnable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this , ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(QuestionViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        correctAnswer = view.findViewById(R.id.correctAnswerTv);
        wrongAnswer = view.findViewById(R.id.wrongAnswersTv);
        notAnswered = view.findViewById(R.id.notAnsweredTv);
        percentTv = view.findViewById(R.id.resultPercentageTv);
        scoreProgressbar = view.findViewById(R.id.resultCoutProgressBar);
        judgement=view.findViewById(R.id.judgement);
        homeBtn = view.findViewById(R.id.home_btn);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_resultFragment_to_listFragment);

                handler = new Handler(Looper.getMainLooper());
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Please continue your process!", Toast.LENGTH_SHORT).show();
                    }
                };
                handler.postDelayed(runnable, 10000);
            }
        });


        quizId = ResultFragmentArgs.fromBundle(getArguments()).getQuizId();

        viewModel.setQuizId(quizId);
        viewModel.getResults();
        viewModel.getResultMutableLiveData().observe(getViewLifecycleOwner(), new Observer<HashMap<String, Long>>() {
            @Override
            public void onChanged(HashMap<String, Long> stringLongHashMap) {
                Long correct = stringLongHashMap.get("correct");
                Long wrong = stringLongHashMap.get("wrong");
                Long noAnswer = stringLongHashMap.get("notAnswered");

                correctAnswer.setText(correct.toString());
                wrongAnswer.setText(wrong.toString());
                notAnswered.setText(noAnswer.toString());

                Long total = correct + wrong + noAnswer;
                Long percent = (correct*100)/total;

                percentTv.setText(String.valueOf(percent));
                scoreProgressbar.setProgress(percent.intValue());

                if (percent >= 90) {
                    judgement.setText("Excellence!!!");
                    judgement.setTextColor(getResources().getColor(R.color.green));
                } else if (percent >= 70 && percent < 90) {
                    judgement.setText("Good job!!");
                    judgement.setTextColor(getResources().getColor(R.color.blue_200));
                } else {
                    judgement.setText("Please try it again!");
                    judgement.setTextColor(getResources().getColor(R.color.red));
                }
            }
        });

    }


}