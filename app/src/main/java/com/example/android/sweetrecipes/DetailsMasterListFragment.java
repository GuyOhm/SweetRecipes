package com.example.android.sweetrecipes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.sweetrecipes.adapter.RecipeDetailsAdapter;
import com.example.android.sweetrecipes.model.Ingredient;
import com.example.android.sweetrecipes.model.Recipe;
import com.example.android.sweetrecipes.model.Step;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.sweetrecipes.MainActivity.RECIPE_EXTRA;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnDetailClickListener} interface
 * to handle interaction events.
 * Use the {@link DetailsMasterListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsMasterListFragment extends Fragment implements RecipeDetailsAdapter.RecipeDetailsAdapterListener{

    private static final String LOG_TAG = DetailsMasterListFragment.class.getSimpleName();

/*    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;*/

    private OnDetailClickListener mListener;

    public DetailsMasterListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param
     * @param
     * @return A new instance of fragment DetailsMasterListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsMasterListFragment newInstance(/*String param1, String param2*/) {
        DetailsMasterListFragment fragment = new DetailsMasterListFragment();
/*        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_details_master_list, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.details_list_rv);

        List<Ingredient> ingredients = new ArrayList<>();
        List<Step> steps = new ArrayList<>();

        if (getActivity() != null && isAdded()) {
            Intent intent = getActivity().getIntent();
            if (intent.hasExtra(RECIPE_EXTRA)) {
                Recipe recipe = intent.getParcelableExtra(RECIPE_EXTRA);
                ingredients = recipe.getIngredients();
                steps = recipe.getSteps();
            }
        }

        RecipeDetailsAdapter adapter = new RecipeDetailsAdapter(getActivity(), ingredients, steps, this);
        // Create a layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

/*    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onDetailSelected(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailClickListener) {
            mListener = (OnDetailClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDetailClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onIngredientsSelected(List<Ingredient> ingredients) {
        if (mListener != null) {
            mListener.onDetailIngredientSelected(ingredients);
        }
    }

    @Override
    public void onStepSelected(Step step) {
        if (mListener != null) {
            mListener.onDetailStepSelected(step);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnDetailClickListener {
        void onDetailIngredientSelected(List<Ingredient> ingredients);
        void onDetailStepSelected(Step step);
    }
}
