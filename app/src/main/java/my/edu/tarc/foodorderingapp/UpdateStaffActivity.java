package my.edu.tarc.foodorderingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateStaffActivity extends AppCompatActivity {

    ProgressDialog pd;
    private Button browse, updateStaff;
    private ImageView staffPic;
    private EditText textId,textName, textIc, textContact, textEmail;
    private Spinner spinnerPosition, spinnerStatus, staffID;
    private Bitmap bitmap, bitmap2=null;
    FirebaseFirestore mFirestore;
    private static final int PICK_IMAGE = 1;
    private int temp = 0;

    List<Staff> staffList = new ArrayList<>();
    List<String> staffIdSpinner = new ArrayList<>();
    Staff staffItem;
    ArrayAdapter staffIdList, statusList, positionList;

    String spinnerStatusItem [] ={"Working","Resign","Retire","MIA"};
    String spinnerPositionItem [] = {"Chef","Cleaner","Cashier","Manager","Waiter"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_staff);

        browse = (Button) findViewById(R.id.buttonBrowse2);
        staffPic = (ImageView) findViewById(R.id.imageViewStaff2);
        updateStaff = (Button) findViewById(R.id.buttonUpdateStaff);
        staffID = (Spinner) findViewById(R.id.spinnerStaffId);
        textName = (EditText) findViewById(R.id.editTextStaffName2);
        textContact = (EditText) findViewById(R.id.editTextStaffContact2);
        textEmail = (EditText) findViewById(R.id.editTextStaffEmail2);
        spinnerPosition = (Spinner) findViewById(R.id.spinnerStaffPosition2);
        spinnerStatus = (Spinner) findViewById(R.id.spinnerStaffStatus2);
        pd = new ProgressDialog(UpdateStaffActivity.this);

        statusList = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerStatusItem);
        statusList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusList);

        positionList = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerPositionItem);
        positionList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPosition.setAdapter(positionList);

        mFirestore = FirebaseFirestore.getInstance();
        loadActivity();

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath());
                intent.setDataAndType(uri, "image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        updateStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Please Wait...");
                pd.setCancelable(false);
                pd.setCanceledOnTouchOutside(true);
                pd.show();
                mFirestore.collection("Staff").whereEqualTo("staffID", staffList.get(temp).getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Map<String,Object> staffMap = new HashMap<>();

                            String pic;
                            if(bitmap2 == null)
                                pic = BitMapToString(bitmap);
                            else
                                pic = BitMapToString(bitmap2);
                            staffMap.put("staffName",textName.getText().toString());
                            staffMap.put("staffPosition",spinnerPosition.getSelectedItem().toString());
                            staffMap.put("staffContact", textContact.getText().toString());
                            staffMap.put("staffEmail",textEmail.getText().toString());
                            staffMap.put("staffStatus",spinnerStatus.getSelectedItem().toString());
                            staffMap.put("staffPicture", pic);
                            String docID="";
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                docID = documentSnapshot.getId();

                            }
                            mFirestore.collection("Staff").document(docID).update(staffMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(UpdateStaffActivity.this, "Update Successfull", Toast.LENGTH_SHORT).show();
                                    loadActivity();
                                    staffList.clear();
                                    pd.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UpdateStaffActivity.this, "Update Fail", Toast.LENGTH_SHORT).show();
                                    pd.dismiss();
                                }
                            });
                        }
                    }
                });

            }
        });
    }
    private void loadActivity(){
        pd.dismiss();
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(true);
        pd.show();
        staffList.clear();
        staffID.setAdapter(null);
        staffIdList=null;
        staffIdSpinner.clear();
        mFirestore.collection("Staff").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                        String id = ""+documentSnapshot.getData().get("staffID").toString();
                        staffIdSpinner.add(id);
                        String pic = ""+documentSnapshot.getData().get("staffPicture").toString();
                        String name = ""+documentSnapshot.getData().get("staffName").toString();
                        String ic = ""+documentSnapshot.getData().get("staffic").toString();
                        String position = ""+documentSnapshot.getData().get("staffPosition").toString();
                        String dob = ""+documentSnapshot.getData().get("staffDOB").toString();
                        String gender = ""+documentSnapshot.getData().get("staffGender").toString();
                        String contact = ""+documentSnapshot.getData().get("staffContact").toString();
                        String email = ""+documentSnapshot.getData().get("staffEmail").toString();
                        String status = ""+documentSnapshot.getData().get("staffStatus").toString();

                        staffItem=new Staff(id, pic,name,dob,position,ic,gender,contact,email,status);
                        staffList.add(staffItem);
                    }

                    staffIdList = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, staffIdSpinner);
                    staffIdList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    staffID.setAdapter(staffIdList);

                    pd.dismiss();
                    staffID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            bitmap = StringToBitMap(""+staffList.get(i).getStaffPic());
                            staffPic.setImageBitmap(bitmap);

                            textName.setText(""+staffList.get(i).getStaffName());

                            textContact.setText(""+staffList.get(i).getContact());

                            textEmail.setText(""+staffList.get(i).getEmail());
                            spinnerPosition.setSelection(positionList.getPosition(""+staffList.get(i).getPosition()));
                            spinnerStatus.setSelection(statusList.getPosition(""+staffList.get(i).getStatus()));


                            temp = i;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                }

            }
        });
    }
    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap btp= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return btp;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
    public String BitMapToString(Bitmap bm){
        ByteArrayOutputStream ByteStream=new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,10, ByteStream);
        //bm.compress(Bitmap.CompressFormat.JPEG, 100,ByteStream);
        byte [] b=ByteStream.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            try {
                bitmap2 = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            staffPic = (ImageView)findViewById(R.id.imageViewStaff2);
            staffPic.setImageBitmap(bitmap2);




        }
    }
}