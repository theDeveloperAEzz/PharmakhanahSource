package com.pharmakhanah.hp.pharmakhanahsource.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
//import com.pharmacy.hp.pharmakhanah.DB.DBHelper;
//import com.pharmacy.hp.pharmakhanah.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pharmakhanah.hp.pharmakhanahsource.DB.DBHelper;
import com.pharmakhanah.hp.pharmakhanahsource.R;
import com.pharmakhanah.hp.pharmakhanahsource.activity.LoginActivity;
import com.pharmakhanah.hp.pharmakhanahsource.model.UserInformation;
import com.pharmakhanah.hp.pharmakhanahsource.model.adapters.ProfilePostsAdapter;
import com.pharmakhanah.hp.pharmakhanahsource.util.Utility;
import com.squareup.picasso.Picasso;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;

//import static com.facebook.FacebookSdk.getApplicationContext;

public class ProfileFragment extends Fragment {
    ImageView imageViewProfilePic;
    TextView textViewProfilePic;
    TextView mTextViewNoPostsProfile;
    TextView textViewUserName;
    TextView textViewUserEmail;
    TextView textViewUserPhone;
    TextView textViewUserCity;
    EditText editTextUsername, editTextUserPhone;
    MaterialBetterSpinner spinnerEditCity;
    ArrayList englishGov;
    ArrayList arabicGov;
    ArrayAdapter adapterSpinner;
    public static FloatingActionButton fabProfile;
    Button btnEditPic;
    ProgressBar progressBar;
    UserInformation userInformation;
    RecyclerView mRecyclerViewPostsProfile;
    ProfilePostsAdapter profilePostsAdapter;
    ArrayList postsProfileArrayList;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    DBHelper dbHelper;
    UploadTask uploadTask;
    Uri selectedImage;
    StorageReference storageRef, imageRef;
    int REQUEST_CAMERA = 1;
    int SELECT_FILE = 2;
    String userChosenTask;
    String imgurl;
    String email = "";
    String name = "";
    String phone = "";
    String oldCity, city = "";
    String uId = "";
    String avatar = "";
    boolean b = false;
    int x = 0;
    View rootView;
    ValueEventListener userInformationListener = new ValueEventListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @SuppressLint("SetTextI18n")
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot != null) {
                userInformation = dataSnapshot.getValue(UserInformation.class);
                saveAndReviewUserInformation(userInformation);

            }


        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        initView();
        fabProfile.setImageResource(R.mipmap.ic_edit);
        dbHelper = new DBHelper(getContext());
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();
        mTextViewNoPostsProfile.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        getUserData();
        setImageViewProfilePic();
        setBtnEditPic();
        setFabProfile();
        postsProfileArrayList = new ArrayList();
        postsProfileArrayList.add("kjbjkj");
        postsProfileArrayList.add("kjbjkj");
        postsProfileArrayList.add("kjbjkj");
        displayUserPostsList(postsProfileArrayList);

        return rootView;
    }

    void setBtnEditPic() {
        btnEditPic.setOnClickListener(
                new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View v) {
                        if (isOnline()) {
                            selectImage();
                            displayUserInformation();
                        } else {
                            Toast.makeText(getContext(), R.string.check_internet_connection, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void setImageViewProfilePic() {
        imageViewProfilePic.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View v) {
                if (!dbHelper.selectUserInformation().get(0).getAvatar().equals("")) {
                    viewProfilePic();
                } else if (!textViewProfilePic.getText().equals("")) {
                    Toast.makeText(getContext(), R.string.you_havent_set_photo_yet, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void setFabProfile() {
        fabProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                englishGov = new ArrayList();
                arabicGov = new ArrayList();
                name = textViewUserName.getText().toString();
                phone = textViewUserPhone.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View dialogView = inflater.inflate(R.layout.alert_edit_information, null);
                editTextUsername = dialogView.findViewById(R.id.edit_user_name);
                editTextUserPhone = dialogView.findViewById(R.id.edit_user_phone);
                spinnerEditCity = dialogView.findViewById(R.id.spinner_edit_city);
                handleSpinner();
                oldCity = spinnerEditCity.getText().toString();
                editTextUsername.setText(name);
                editTextUserPhone.setText(phone);
                //                spinnerEditCity.setText(city);
                builder.setView(dialogView);
                builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        String newName, newPhone;
                        newName = editTextUsername.getText().toString();
                        newPhone = editTextUserPhone.getText().toString();
                        oldCity = spinnerEditCity.getText().toString();

                        if (!newName.equals("") && !newName.equals(name)) {
                            databaseReference.child("UsersInformation").child("Users")
                                    .child(firebaseAuth.getCurrentUser().getUid()).child("name").setValue(newName);
                            textViewUserName.setText(newName);
                        }
                        if (!newPhone.equals("") && !newPhone.equals(phone)) {
                            databaseReference.child("UsersInformation").child("Users")
                                    .child(firebaseAuth.getCurrentUser().getUid()).child("phone").setValue(newPhone);
                            textViewUserPhone.setText(newPhone);
                        }
                        if (!city.equals("")) {
                            databaseReference.child("UsersInformation").child("Users")
                                    .child(firebaseAuth.getCurrentUser().getUid()).child("city").setValue(city);
                            textViewUserCity.setText(city);
                        }

                        displayUserInformation();
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    public void handleSpinner() {
        if (Locale.getDefault().getLanguage().equals("en")) {
            String[] arr1 = getResources().getStringArray(R.array.englishGov);
            for (String anArr1 : arr1) {
                city = anArr1;
                if (city.length() > 0 && !englishGov.contains(city)) {
                    englishGov.add(city);
                }
            }
            city = "";
            Collections.sort(englishGov, String.CASE_INSENSITIVE_ORDER);
            adapterSpinner = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, englishGov);
            spinnerEditCity.setAdapter(adapterSpinner);

        } else {
            String[] arr2 = getResources().getStringArray(R.array.arabicGov);
            for (String anArr2 : arr2) {
                city = anArr2;
                if (city.length() > 0 && !arabicGov.contains(city)) {
                    arabicGov.add(city);
                }
            }
            Collections.sort(arabicGov, String.CASE_INSENSITIVE_ORDER);
            adapterSpinner = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, arabicGov);
            spinnerEditCity.setAdapter(adapterSpinner);
        }
        spinnerEditCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(SignUpActivity.this, "selected", Toast.LENGTH_SHORT).show();
                city = parent.getItemAtPosition(position).toString();
                b = true;
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void getUserData() {
        if (dbHelper.selectUserInformation().size() != 0) {
            getAndReviewLocaleUserInformation();
        } else {
            displayUserInformation();
        }
    }

    void displayUserPostsList(ArrayList postsProfileArrayList) {
        profilePostsAdapter = new ProfilePostsAdapter(postsProfileArrayList);
        mRecyclerViewPostsProfile.setNestedScrollingEnabled(false);
        mRecyclerViewPostsProfile.setAdapter(profilePostsAdapter);
        mRecyclerViewPostsProfile.setLayoutManager(new LinearLayoutManager(getContext()));
        if (profilePostsAdapter.getItemCount() == 0) {
            mTextViewNoPostsProfile.setVisibility(View.VISIBLE);
        }
    }

    public void initView() {
        imageViewProfilePic = rootView.findViewById(R.id.profile_pic);
        textViewProfilePic = rootView.findViewById(R.id.txt_profile_pic);
        textViewUserName = rootView.findViewById(R.id.txt_name);
        textViewUserEmail = rootView.findViewById(R.id.txt_email);
        textViewUserPhone = rootView.findViewById(R.id.txt_phone);
        textViewUserCity = rootView.findViewById(R.id.txt_city);
        mTextViewNoPostsProfile = rootView.findViewById(R.id.txt_no_posts_profile);
        mRecyclerViewPostsProfile = rootView.findViewById(R.id.recycle_my_posts);
        progressBar = rootView.findViewById(R.id.progressbar_profile);
        btnEditPic = rootView.findViewById(R.id.btn_edit_image);
        fabProfile = rootView.findViewById(R.id.fab_profile);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void displayUserInformation() {
        if (isOnline()) {
            uId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
            databaseReference.child("UsersInformation").child("Users")
                    .child(uId).addValueEventListener(userInformationListener);
        } else {
            if (dbHelper.selectUserInformation().size() != 0) {
                getAndReviewLocaleUserInformation();
                Toast.makeText(getContext(), "check internet connection", Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "check internet connection", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    void saveAndReviewUserInformation(final UserInformation userInformation) {
        assert userInformation != null;
        //save image to SQLite
        name = userInformation.getName();
        email = userInformation.getEmail();
        phone = userInformation.getPhone();
        city = userInformation.getCity();
        avatar = userInformation.getAvatar();
        textViewUserName.setText(name);
        textViewUserEmail.setText(email);
        textViewUserCity.setText(city);
        if (!phone.equals("")) {
            textViewUserPhone.setText(phone);
        }
        if (!avatar.equals("")) {
            Picasso.with(getContext()).load(avatar).into(imageViewProfilePic);
            dbHelper.clearTable();
            getImageAsBitmap(userInformation);
        } else {
            textViewProfilePic.setText(name.charAt(0) + "");
            dbHelper.clearTable();
            dbHelper.insert(userInformation);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressLint("SetTextI18n")
    void getAndReviewLocaleUserInformation() {
        String avatar, name, txtProfilePic, email, phone, city;
        name = dbHelper.selectUserInformation().get(0).getName();
        avatar = dbHelper.selectUserInformation().get(0).getAvatar();
        txtProfilePic = name.charAt(0) + "";
        email = dbHelper.selectUserInformation().get(0).getEmail();
        phone = dbHelper.selectUserInformation().get(0).getPhone();
        city = dbHelper.selectUserInformation().get(0).getCity();
        textViewUserName.setText(name);
        textViewUserEmail.setText(email);
        textViewProfilePic.setText(txtProfilePic);
        textViewUserPhone.setText(phone);
        textViewUserCity.setText(city);
        if (!avatar.equals("")) {
            Bitmap myBitmapAgain = decodeBase64(avatar);
            imageViewProfilePic.setImageBitmap(myBitmapAgain);
        } else {
            textViewProfilePic.setText(name.charAt(0) + "");
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel",};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getContext());
                if (items[item].equals("Take Photo")) {
                    userChosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        String[] path = {MediaStore.Images.Media.DATA};
        //show image that camera took
        imageViewProfilePic.setImageBitmap(BitmapFactory.decodeFile(Arrays.toString(path), options));
//        UploadBitmap(BitmapFactory.decodeFile(Arrays.toString(path), options));
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "SelectName File"), SELECT_FILE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChosenTask.equals("Choose from Library"))
                        galleryIntent();
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            selectedImage = data.getData();
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imageViewProfilePic.setImageBitmap(resizedBitmap(bm));
        if (isOnline()) {
            UploadBitmap(resizedBitmap(bm));
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void onCaptureImageResult(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");

        imageViewProfilePic.setImageBitmap(resizedBitmap(imageBitmap));
        if (isOnline()) {
            UploadBitmap(resizedBitmap(imageBitmap));
        }
    }

    public void UploadBitmap(Bitmap bitmap) {
        if (bitmap.getByteCount() < 3000000) {
            progressBar.setVisibility(View.VISIBLE);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            imageRef = storageRef.child(firebaseAuth.getCurrentUser().getUid() + ".jpg");
            uploadTask = imageRef.putBytes(data);
            uploadTaskOnSuccessListener();
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            Toast.makeText(getContext(), "high image", Toast.LENGTH_SHORT).show();
        }
    }

    void uploadTaskOnSuccessListener() {
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.child(firebaseAuth.getCurrentUser().getUid() + ".jpg")
                        .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imgurl = uri.toString();
                        setAvatarUrl();
                    }
                });
            }
        });

    }

    void setAvatarUrl() {
        databaseReference.child("UsersInformation").child("Users")
                .child(firebaseAuth.getCurrentUser().getUid()).child("avatar").setValue(imgurl)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                }).addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "faild" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void getImageAsBitmap(final UserInformation userInformation) {
        ImageRequest imageRequest = new ImageRequest(
                userInformation.getAvatar(), // Image URL
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        // convert from url's poster_path to base64 for save it in SQLite
//                        imageViewProfilePic.setImageBitmap(response);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        response.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        userInformation.setAvatar(encoded);
                        //the solution is delete all contacts if connected
                        dbHelper.insert(userInformation);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                },
                0, // Image width
                0, // Image height
                ImageView.ScaleType.CENTER_CROP, // Image scale type
                Bitmap.Config.RGB_565, //Image decode configuration
                new Response.ErrorListener() { // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something with error response
                        error.printStackTrace();

                    }
                }
        );
        Volley.newRequestQueue(Objects.requireNonNull(getContext())).add(imageRequest);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    void viewProfilePic() {
        final Dialog nagDialog = new Dialog(Objects.requireNonNull(getActivity()), android.R.style.Theme_DeviceDefault_Light_Dialog);
        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nagDialog.setCancelable(true);
        nagDialog.setContentView(R.layout.preview_image);
        Button btnClose = (Button) nagDialog.findViewById(R.id.btnIvClose);
        ImageView ivPreview = (ImageView) nagDialog.findViewById(R.id.iv_preview_image);
        ivPreview.setBackgroundDrawable(imageViewProfilePic.getDrawable());
        if (imageViewProfilePic.getDrawable() == null) {
            btnClose.setBackgroundResource(R.drawable.met_ic_close);
        }
        ivPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                nagDialog.dismiss();
            }
        });
        nagDialog.show();
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private Bitmap decodeFile(String imgPath) {
        Bitmap b = null;
        int max_size = 1000;
        File f = new File(imgPath);
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
            int scale = 1;
            if (o.outHeight > max_size || o.outWidth > max_size) {
                scale = (int) Math.pow(2, (int) Math.ceil(Math.log(max_size /
                        (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (Exception e) {
        }
        return b;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        @SuppressLint("Recycle")
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private Bitmap resizedBitmap(Bitmap bm) {
        Uri tempUri = getImageUri(getContext(), bm);
        String path = getRealPathFromURI(tempUri);
        return decodeFile(path);
    }

}
