package com.pharmakhanah.hp.pharmakhanahsource.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pharmakhanah.hp.pharmakhanahsource.DB.DBHelper;
import com.pharmakhanah.hp.pharmakhanahsource.R;
import com.pharmakhanah.hp.pharmakhanahsource.model.MedicineObjectModel;
import com.pharmakhanah.hp.pharmakhanahsource.model.PostObjectModel;
import com.pharmakhanah.hp.pharmakhanahsource.model.UserInformation;
import com.pharmakhanah.hp.pharmakhanahsource.model.adapters.AutocompleteCustomArrayAdapter;
import com.pharmakhanah.hp.pharmakhanahsource.model.adapters.HomePostAdapter;
import com.pharmakhanah.hp.pharmakhanahsource.util.EndlessScrollListener;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

import static com.pharmakhanah.hp.pharmakhanahsource.fragment.ProfileFragment.decodeBase64;

public class HomeFragment extends Fragment {
    TextView mTextViewNoPostsHome;
    AutoCompleteTextView inputMedicineNameAutoCompleteTextView;
    ImageView inputImageViewProfilePic;
    MaterialBetterSpinner inputProductionDateSpinner, inputProductionMonthSpinner, inputExpirationDateSpinner, inputExpirationMonthSpinner;
    TextView inputTextViewProfilePic, inputTextViewLoading, inputTextViewCheckConnection, inputTextViewName, inputTextViewCity, inputShelfLifeTextView, inputDosageFormTextView, inputPackageTypeTextView;
    Button inputPostMedicineButton, inputCancelDialogButton;
    RecyclerView mRecyclerViewPostsHome;
    HomePostAdapter mHomePostAdapter;
    ArrayAdapter adapterProductionSpinner, adapterExpirationSpinner, adapterProductionSpinnerMonth, adapterExpirationSpinnerMonth;
    ArrayList yearsArrayList;
    String[] monthsArrayList;
    ArrayList postsHomeArrayList;
    View rootView;
    String url = "https://ahmedezz.000webhostapp.com/php-rest-medicine/read2.php";
    String baseUrl = "https://ahmedezz.000webhostapp.com/php-rest-medicine/";
    ArrayList<MedicineObjectModel> medicineDataModelArrayList;
    ArrayList medicineNameSuggestions;
    HashSet dosageFormHashSet, packageTypeHashSet;
    List<MedicineObjectModel> medicineDataList;
    DBHelper dbHelper;
    View dialogView;
    int productYear, expirationYear;
    String productMonth, expirationMonth;
    AutocompleteCustomArrayAdapter autocompleteCustomArrayAdapter;
    AlertDialog dialog;
    ProgressBar progressBar;
    String baseUrlAddNewMed = "https://ahmedezz.000webhostapp.com";
    String inputMedicineUId, inputMedicineName, inputMedicineDosageForm, inputMedicinePackageType,
            inputMedicineProductionDate, inputMedicineExpirationDate, inputMedicineShelfLife;
    String currentUserUID, currentUserAvatar, currentUserName, currentUserCity, currentUserPhoneNumber;
    Long currentUserPostTime;
    TextView textViewPostNewMedicine;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
            currentUserAvatar = userInformation.getAvatar();
            currentUserName = userInformation.getName();
            currentUserCity = userInformation.getCity();
            currentUserPhoneNumber = userInformation.getPhone();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUser = firebaseAuth.getCurrentUser();
        currentUserUID = currentUser.getUid();
//        databaseReference.child("UsersInformation").child("Users").child(currentUserUID).addValueEventListener(valueEventListener);
        postsHomeArrayList = new ArrayList();
        medicineDataList = new ArrayList<>();
        medicineNameSuggestions = new ArrayList();
        dosageFormHashSet = new HashSet();
        packageTypeHashSet = new HashSet();
        dbHelper = new DBHelper(getContext());
        postsHomeArrayList.add("njd");
        postsHomeArrayList.add("njd");
        postsHomeArrayList.add("njd");
        displayHomePostsList(postsHomeArrayList);
        textViewPostNewMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postNewMedicine();

            }
        });

        return rootView;
    }

    void postNewMedicine() {
        yearsArrayList = new ArrayList();
        monthsArrayList = new DateFormatSymbols().getMonths();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int lastTenTears = currentYear - 10;
        for (int x = 0; x < 21; x++) {
            yearsArrayList.add(lastTenTears + x);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        dialogView = inflater.inflate(R.layout.layout_post_medicine, null);
        initDialogView();
        progressBar.setVisibility(View.VISIBLE);
        inputTextViewLoading.setVisibility(View.VISIBLE);
        getAndReviewLocaleUserInformation();
        getRetrofitData();

        handleSpinner();
        builder.setView(dialogView);
        dialog = builder.create();
        dialog.show();
        inputPostMedicineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               progressBar.setVisibility(View.VISIBLE);
                if (!medicineNameSuggestions.contains(inputMedicineNameAutoCompleteTextView.getText().toString()) ||
                        inputMedicineNameAutoCompleteTextView.getText().toString().equals("")) {
                    inputMedicineNameAutoCompleteTextView.setError("invalid name");
                    inputMedicineNameAutoCompleteTextView.setTextColor(getResources().getColor(R.color.black));
                    inputShelfLifeTextView.setText("");
                    inputDosageFormTextView.setText("");
                    inputPackageTypeTextView.setText("");
                    return;
                }
                if (inputProductionDateSpinner.getText().toString().equals("")) {
                    inputProductionDateSpinner.setError("choose production year");
                    return;
                }
                if (inputProductionMonthSpinner.getText().toString().equals("")) {
                    inputProductionMonthSpinner.setError("choose production month");
                    return;
                }
                if (inputExpirationDateSpinner.getText().toString().equals("")) {
                    inputExpirationDateSpinner.setError("choose expiration year");
                    return;
                }
                if (inputExpirationMonthSpinner.getText().toString().equals("")) {
                    inputExpirationMonthSpinner.setError("choose expiration month");
                    return;
                }
                inputMedicineUId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                inputMedicineName = inputMedicineNameAutoCompleteTextView.getText().toString();
                inputMedicineDosageForm = inputDosageFormTextView.getText().toString();
                inputMedicinePackageType = inputPackageTypeTextView.getText().toString();
                inputMedicineProductionDate = inputProductionDateSpinner.getText().toString() + "-"
                        + inputProductionMonthSpinner.getText().toString();
                inputMedicineExpirationDate = inputExpirationDateSpinner.getText().toString() + "-"
                        + inputExpirationMonthSpinner.getText().toString();
                inputMedicineShelfLife = inputShelfLifeTextView.getText().toString();
                currentUserAvatar = dbHelper.selectUserInformation().get(0).getAvatar();
                currentUserName = dbHelper.selectUserInformation().get(0).getName();
                currentUserCity = dbHelper.selectUserInformation().get(0).getCity();
//                String time = new SimpleDateFormat("EEE, d MMM yyyy").format(chat.getTime());
//                String today = new SimpleDateFormat("EEE, d MMM yyyy").format(new Date(System.currentTimeMillis()));
//                if (today.equals(time)) {
//                    holder.mTextViewTime.setText(new SimpleDateFormat("hh:mm a").format(new Date(chat.getTime())));
//                } else {
//                    holder.mTextViewTime.setText(new SimpleDateFormat("hh:mm a \nd MMM ").format(new Date(chat.getTime())));
//                }
                currentUserPostTime = System.currentTimeMillis();
                currentUserPhoneNumber = dbHelper.selectUserInformation().get(0).getPhone();
                postMedicineRetrofit();
//                postMedicineVolley(inputMedicineUId,inputMedicineName, inputMedicineDosageForm, inputMedicinePackageType
//                        , inputMedicineProductionDate, inputMedicineExpirationDate, inputMedicineShelfLife);
                dialog.dismiss();

            }
        });
        inputCancelDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    void displayHomePostsList(ArrayList postsProfileArrayList) {
        mHomePostAdapter = new HomePostAdapter(postsHomeArrayList);
        mRecyclerViewPostsHome.setNestedScrollingEnabled(false);
        mRecyclerViewPostsHome.smoothScrollToPosition(1);
        mRecyclerViewPostsHome.setAdapter(mHomePostAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewPostsHome.setLayoutManager(linearLayoutManager);
        if (mHomePostAdapter.getItemCount() == 0) {
            mTextViewNoPostsHome.setVisibility(View.VISIBLE);
        }
        mRecyclerViewPostsHome.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                return false;
            }
        });
    }

    public void handleSpinner() {
        adapterProductionSpinner = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, yearsArrayList);
        inputProductionDateSpinner.setAdapter(adapterProductionSpinner);


        inputExpirationDateSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                productYear = (int) parent.getItemAtPosition(position);

            }
        });
        adapterExpirationSpinner = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, yearsArrayList);
        inputExpirationDateSpinner.setAdapter(adapterExpirationSpinner);
        inputExpirationDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                expirationYear = (int) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapterProductionSpinnerMonth = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, monthsArrayList);
        inputProductionMonthSpinner.setAdapter(adapterProductionSpinnerMonth);
        inputProductionMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productMonth = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapterExpirationSpinnerMonth = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, monthsArrayList);
        inputExpirationMonthSpinner.setAdapter(adapterExpirationSpinnerMonth);
        inputExpirationMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                expirationMonth = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    void initView() {
        mTextViewNoPostsHome = rootView.findViewById(R.id.txt_no_posts_home);
        mTextViewNoPostsHome.setVisibility(View.INVISIBLE);
        mRecyclerViewPostsHome = rootView.findViewById(R.id.recycle_posts_home);
        textViewPostNewMedicine = rootView.findViewById(R.id.txt_post_medicine);
    }

    void initDialogView() {
        inputImageViewProfilePic = dialogView.findViewById(R.id.author_pic);
        inputTextViewName = dialogView.findViewById(R.id.author_name);
        inputTextViewCity = dialogView.findViewById(R.id.author_city);
        inputMedicineNameAutoCompleteTextView = dialogView.findViewById(R.id.edit_choose_medicine_name);
        inputShelfLifeTextView = dialogView.findViewById(R.id.shelf_life);
        inputDosageFormTextView = dialogView.findViewById(R.id.dosage_form);
        inputPackageTypeTextView = dialogView.findViewById(R.id.package_type);
        inputPostMedicineButton = dialogView.findViewById(R.id.btn_post);
        inputCancelDialogButton = dialogView.findViewById(R.id.btn_cancel);
        inputProductionDateSpinner = dialogView.findViewById(R.id.spinner_production_date);
        inputProductionMonthSpinner = dialogView.findViewById(R.id.spinner_production_date_month);
        inputExpirationDateSpinner = dialogView.findViewById(R.id.spinner_expiration_date);
        inputExpirationMonthSpinner = dialogView.findViewById(R.id.spinner_expiration_date_month);
        progressBar = dialogView.findViewById(R.id.progressbar_home);
        inputTextViewCheckConnection = dialogView.findViewById(R.id.text_check_connection);
        inputTextViewLoading = dialogView.findViewById(R.id.text_loading);
    }

    @SuppressLint("SetTextI18n")
    void getAndReviewLocaleUserInformation() {
        String avatar, name, txtProfilePic, city;
        name = dbHelper.selectUserInformation().get(0).getName();
        avatar = dbHelper.selectUserInformation().get(0).getAvatar();
        txtProfilePic = name.charAt(0) + "";
        city = dbHelper.selectUserInformation().get(0).getCity();
        if (!avatar.equals("")) {
            Bitmap myBitmapAgain = decodeBase64(avatar);
            inputImageViewProfilePic.setImageBitmap(myBitmapAgain);
        } else {
            inputTextViewProfilePic.setText(txtProfilePic);
        }
        inputTextViewName.setText(name);
        inputTextViewCity.setText(city);
    }

    void getRetrofitData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ReadMedicineNameAndDataApi readMedicineNameAndDataApi =
                retrofit.create(ReadMedicineNameAndDataApi.class);
        Call<ResultModel> call = readMedicineNameAndDataApi.getData2();
        call.enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                medicineDataList = response.body().getData();
                for (int x = 0; x < medicineDataList.size(); x++) {
                    medicineNameSuggestions.add(medicineDataList.get(x).getTrade_name());
                }
                progressBar.setVisibility(View.INVISIBLE);
                inputTextViewLoading.setVisibility(View.INVISIBLE);
                inputMedicineNameAutoCompleteTextView.setVisibility(View.VISIBLE);
                autocompleteCustomArrayAdapter =
                        new AutocompleteCustomArrayAdapter(getContext(), medicineDataList);
                inputMedicineNameAutoCompleteTextView.setAdapter(autocompleteCustomArrayAdapter);
                inputMedicineNameAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @SuppressLint({"SetTextI18n"})
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        inputMedicineNameAutoCompleteTextView.setTextColor(getResources().getColor(R.color.green));
                        inputShelfLifeTextView.setText(medicineDataList.get(position).getShelf_life_month() + " " + getString(R.string.shelf_month));
                        inputDosageFormTextView.setText(medicineDataList.get(position).getDosage_form());
                        inputPackageTypeTextView.setText(medicineDataList.get(position).getPackage_type());

                    }
                });


                //                MedicineObjectModel medicineObjectMode = new MedicineObjectModel();
//                medicineObjectMode = medicineDataList.get(0);
//                dbHelper.clearMedicineObjectModelTable();
//                dbHelper.insertMedicineObjectModel(medicineObjectMode);
////                String s= dbHelper.selectMedicineObjectModelArrayList().get(0).getTrade_name();
//                ArrayList<MedicineObjectModel> arrayList = new ArrayList<>();
//                arrayList = dbHelper.selectMedicineObjectModelArrayList();
//
//               for (int x = 0; x < medicineDataList.size(); x++) {
//                    dosageFormHashSet.add(medicineDataList.get(x).getDosage_form());
//                    packageTypeHashSet.add(medicineDataList.get(x).getPackage_type());
//                }

            }

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                inputTextViewLoading.setVisibility(View.INVISIBLE);
                inputTextViewCheckConnection.setVisibility(View.VISIBLE);
                inputTextViewCheckConnection.setText(R.string.check_connection);
            }
        });
    }

    void getVolleyData() {
        medicineDataModelArrayList = new ArrayList<>();
        medicineNameSuggestions = new ArrayList();
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonElements = response.getJSONArray("data");
                            for (int x = 0; x < jsonElements.length(); x++) {
                                JSONObject object = jsonElements.getJSONObject(x);
                                String id = object.getString("id");
                                String generic_name = object.getString("generic_name");
                                String trade_name = object.getString("trade_name");
                                String dosage_form = object.getString("dosage_form");
                                String route_of_administration = object.getString("route_of_administration");
                                String package_type = object.getString("package_type");
                                String product_control = object.getString("product_control");
                                String shelf_life_month = object.getString("shelf_life_month");
                                String storage_conditions = object.getString("storage_conditions");

                                MedicineObjectModel medicineObjectModel = new MedicineObjectModel(id, generic_name, trade_name, dosage_form, route_of_administration,
                                        package_type, product_control, shelf_life_month, storage_conditions);
                                medicineDataModelArrayList.add(medicineObjectModel);
                                String medicineName = medicineDataModelArrayList.get(x).getGeneric_name();
                                medicineNameSuggestions.add(medicineName);
                            }
                            Toast.makeText(getContext(), "succses", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }

    public interface ReadMedicineNameAndDataApi {
        @GET("read2.php")
        Call<ResultModel> getData2();

        @FormUrlEncoded
        @POST("/php-rest-medicine/addnewmedicine.php")
        Call<PostObjectModel> postMedicine(@Field("m_uid") String m_uid,
                                           @Field("m_medicine_name") String m_medicine_name,
                                           @Field("m_dosage_form") String m_dosage_form,
                                           @Field("m_package_type") String m_package_type,
                                           @Field("m_production_date") String m_production_date,
                                           @Field("m_expiration_date") String m_expiration_date,
                                           @Field("m_shelf_life") String m_shelf_life,
                                           @Field("author_avatar") String author_avatar,
                                           @Field("author_name") String author_name,
                                           @Field("author_city") String author_city,
                                           @Field("author_post_time") String author_post_time,
                                           @Field("author_phone_number") String author_phone_number);
    }

    void postMedicineRetrofit() {
        OkHttpClient client = new OkHttpClient();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrlAddNewMed)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ReadMedicineNameAndDataApi insertPostService =
                retrofit.create(ReadMedicineNameAndDataApi.class);
        PostObjectModel postObjectModel = new PostObjectModel(inputMedicineUId, inputMedicineName, inputMedicineDosageForm,
                inputMedicinePackageType, inputMedicineProductionDate, inputMedicineExpirationDate, inputMedicineShelfLife,
                currentUserAvatar, currentUserName, currentUserCity, currentUserPostTime, currentUserPhoneNumber);

        Call<PostObjectModel> call = insertPostService.postMedicine(postObjectModel.getM_uid(), postObjectModel.getM_medicine_name(), postObjectModel.getM_dosage_form()
                , postObjectModel.getM_package_type(), postObjectModel.getM_production_date(),
                postObjectModel.getM_expiration_date(), postObjectModel.getM_shelf_life(), postObjectModel.getAuthor_avatar(),
                postObjectModel.getAuthor_name(), postObjectModel.getAuthor_city(), String.valueOf(postObjectModel.getAuthor_post_time()),
                postObjectModel.getAuthor_phone_number());

        call.enqueue(new Callback<PostObjectModel>() {
            @Override
            public void onResponse(Call<PostObjectModel> call, Response<PostObjectModel> response) {
                Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<PostObjectModel> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void postMedicineVolley(final String m_uid, final String m_medicine_name, final String m_dosage_form,
                                    final String m_package_type, final String m_production_date,
                                    final String m_expiration_date, final String m_shelf_life) {
        String ADD_TOKEN_URL = "https://ahmedezz.000webhostapp.com/php-rest-medicine/addnewmedicine.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_TOKEN_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), "success", +Toast.LENGTH_LONG).show();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), +Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("m_uid", m_uid);
                params.put("m_medicine_name", m_medicine_name);
                params.put("m_dosage_form", m_dosage_form);
                params.put("m_package_type", m_package_type);
                params.put("m_production_date", m_production_date);
                params.put("m_expiration_date", m_expiration_date);
                params.put("m_shelf_life", m_shelf_life);

                return params;


            }

        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    public class ResultModel {
        private List<MedicineObjectModel> data;

        public List<MedicineObjectModel> getData() {
            return data;
        }

        public void setData(List<MedicineObjectModel> data) {
            this.data = data;
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = null;
        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}
