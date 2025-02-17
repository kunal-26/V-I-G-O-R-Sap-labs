package com.Hatchback.Vigor.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Hatchback.Vigor.ArticleHome.ArticleHome;
import com.Hatchback.Vigor.Meditation.MeditationActivity;
import com.Hatchback.Vigor.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    View v;
    private RecyclerView mRecyclerView;
    private MeditationAdapter myAdapter;
    private ArrayList<MeditationModel> listGuides;
    CardView random, journal;
    TextView quote, writer;
    String retrievedQuote, retrievedAuthor;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false);

        // mindful quote segment
        quote = v.findViewById(R.id.quoteTxt);
        writer = v.findViewById(R.id.writerTxt);

        quote.setText(retrievedQuote);
        writer.setText(retrievedAuthor);


        random = v.findViewById(R.id.randomCard);
        journal = v.findViewById(R.id.journalCard);

        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get random category
                String randomGuideName = randomGuide();

                Intent meditationActivity = new Intent(getActivity(), MeditationActivity.class);
                meditationActivity.putExtra("iTitle", randomGuideName);
                meditationActivity.putExtra("iDes", "Press play to listen to your guide!");
                startActivity(meditationActivity);
            }
        });

        journal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent articleActivity = new Intent(getActivity(), ArticleHome.class);
                startActivity(articleActivity);

            }
        });

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        MeditationAdapter recycleradpter = new MeditationAdapter(getContext(),listGuides);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity())) ;  //create recycler view in linearlayout
//        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2)) ;  //create recycler view in linearlayout
        mRecyclerView.setAdapter(recycleradpter);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //get quote shared pref
        SharedPreferences homePref = this.getActivity().getSharedPreferences("com.android.meditate.Home", Context.MODE_PRIVATE);

        // retrieve date
        String retrievedDate = homePref.getString("Date", "");

        // not same day
        if (!retrievedDate.equalsIgnoreCase(getDateTime())){
            // select new quote
            randomQuote(homePref);
        }
        retrievedQuote = homePref.getString("Quote", "“I've decided to be happy because it's good for my health.”");
        retrievedAuthor = homePref.getString("Author", "-Voltaire");


        // create list of meditation guides
        listGuides = new ArrayList<>();
        listGuides.add(new MeditationModel("Sleep", "Feel the night. Watch its beauty.", R.drawable.sleepbg));
        listGuides.add(new MeditationModel("Stress & Anxiety", "Soothes your soul.", R.drawable.stressbg));
        listGuides.add(new MeditationModel("Midnight Thoughts", "A test guide for presentation.", R.drawable.midnightbg));


        try{
            //retrieve purchased guides from shared pref
            SharedPreferences userPref = this.getActivity().getSharedPreferences("com.android.meditate.User", Context.MODE_PRIVATE);
            Set<String> fetch = userPref.getStringSet("purchased", null);

            // add purchase guides to list
            for (String guide: fetch){
                if (guide.equalsIgnoreCase("10 min guides")){
                    listGuides.add(new MeditationModel("10 min guides", "Quick guides anytime anywhere.", R.drawable.sleepbg));
                }
                else if (guide.equalsIgnoreCase("White Noise")){
                    listGuides.add(new MeditationModel("White Noise", "Better sleep. Ease anxiety.", R.drawable.stressbg));
                }
                else if (guide.equalsIgnoreCase("Nature")){
                    listGuides.add(new MeditationModel("Nature", "Love the world as your own self.", R.drawable.midnightbg));
                }
                else if (guide.equalsIgnoreCase("Self care")){
                    listGuides.add(new MeditationModel("Self care", "Accept yourself, love yourself.", R.drawable.selfcarebg));
                }
                else if (guide.equalsIgnoreCase("Rainy days")){
                    listGuides.add(new MeditationModel("Rainy days", "Get warm and comfortable.", R.drawable.rainybg));
                }
                else if (guide.equalsIgnoreCase("Piano")){
                    listGuides.add(new MeditationModel("Piano", "Soothing piano music for you.", R.drawable.pianobg));
                }
                else if (guide.equalsIgnoreCase("Slow down")){
                    listGuides.add(new MeditationModel("Slow down", "A huge part of recovery and life is slowing down.", R.drawable.slowdownbg));
                }
                else{
                    Log.i(TAG, "Error retrieving purchased guides");
                }
            }
        }catch (Exception e){
            Log.i(TAG, "ERROR showing purchased guides");
        }


    }

    public static String getDateTime(){
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        return currentDate;
    }

    public String randomGuide(){
        ArrayList<String> randomList = new ArrayList<>();
        randomList.add("Sleep");
        randomList.add("Stress & Anxiety");
        randomList.add("Midnight Thoughts");

        SharedPreferences userPref = this.getActivity().getSharedPreferences("com.android.meditate.User", Context.MODE_PRIVATE);
        Set<String> fetch = userPref.getStringSet("purchased", null);
        for (String guides: fetch){
            randomList.add(guides);
        }

        Random rand = new Random();
        int index = rand.nextInt(randomList.size());

        return randomList.get(index);
    }


    public static void randomQuote(SharedPreferences homePref){

        ArrayList<QuoteModel> quoteList =  new ArrayList<>();
        quoteList.add(new QuoteModel("“I've decided to be happy because it's good for my health.”", "-Voltaire"));
        quoteList.add(new QuoteModel("“Your goal is not to battle with the mind, but to witness the mind.”", "-Swami Muktananda"));
        quoteList.add(new QuoteModel("“The act of meditation is being spacious.”", "-Sogyal Rinpoche"));
        quoteList.add(new QuoteModel("“Feelings come and go like clouds in a windy sky. Conscious breathing is my anchor.”", "-Thich Nhat Hahn"));

        Random rand = new Random();
        int index = rand.nextInt(quoteList.size());
        homePref.edit().putString("Date", getDateTime()).apply();
        homePref.edit().putString("Quote", quoteList.get(index).getQuote()).apply();
        homePref.edit().putString("Author", quoteList.get(index).getAuthor()).apply();

    }


}
