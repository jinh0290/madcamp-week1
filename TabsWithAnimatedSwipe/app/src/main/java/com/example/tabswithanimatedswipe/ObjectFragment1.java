package com.example.tabswithanimatedswipe;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Instances of this class are fragments representing a single
// object in our collection.
public class ObjectFragment1 extends Fragment implements TextWatcher {
    private static final int MY_PERMISSION_STORAGE = 1111;

    RecyclerView recyclerView;
    EditText editText;
    MyAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    private List<Contact> ContactList;
    Button button_contact;
    Button button_clearing;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_object1, container, false);
        button_contact = view.findViewById(R.id.get_contacts_button);
        button_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContacts();
            }
        });
        button_clearing = view.findViewById(R.id.clearing_button);
        button_clearing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactList.clear();
                mAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        ContactList = new ArrayList<>();

        editText = view.findViewById(R.id.searchBox);
        editText.addTextChangedListener(this);
        recyclerView = view.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        mAdapter = new MyAdapter(ContactList);
        recyclerView.setAdapter(mAdapter);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable {
        private List<Contact> unFilteredList;
        private List<Contact> filteredList;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder

        public class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            private TextView name_view;
            private TextView telNum_view;

            public MyViewHolder(View v) {
                super(v);

                name_view = v.findViewById(R.id.textView_name);
                telNum_view = v.findViewById(R.id.textView_telNum);
            }

            private void onBind(Contact contact) {
                name_view.setText(contact.getName());
                telNum_view.setText(contact.getTelNum());
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(List<Contact> list) {
            super();
            this.unFilteredList = list;
            this.filteredList = list;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_layout, parent, false);

            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.onBind(filteredList.get(position));
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return filteredList.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                // constraint는 검색어
                protected FilterResults performFiltering(CharSequence constraint) {
                    String charString = constraint.toString();
                    if(charString.isEmpty()) {
                        filteredList = unFilteredList;
                    } else {
                        List<Contact> filteringList = new ArrayList<>();
                        for (Contact contact : unFilteredList) {
                            if (contact.getName().toLowerCase().contains(charString.toLowerCase())) {
                                filteringList.add(contact);
                            }
                        }
                        filteredList = filteringList;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredList;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constarint, FilterResults results) {
                    filteredList = (List<Contact>)results.values;
                    notifyDataSetChanged();
                }
            };
        }
    }


    /* Below 3 functions are called by TextChangedListener */
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mAdapter.getFilter().filter(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public void setContacts() {
        checkPermission();
    }

    private void getContacts(Context context) {
        // 데이터베이스 혹은 content resolver 를 통해 가져온 데이터를 적재할 저장소를 먼저 정의
        List<Contact> datas = new ArrayList<>();
        // 1. Resolver 가져오기(데이터베이스 열어주기)
        // 전화번호부에 이미 만들어져 있는 ContentProvider 를 통해 데이터를 가져올 수 있음
        // 다른 앱에 데이터를 제공할 수 있도록 하고 싶으면 ContentProvider 를 설정
        // 핸드폰 기본 앱 들 중 데이터가 존재하는 앱들은 Content Provider 를 갖는다
        // ContentResolver 는 ContentProvider 를 가져오는 통신 수단
        ContentResolver resolver = context.getContentResolver();

        // 2. 전화번호가 저장되어 있는 테이블 주소값(Uri)을 가져오기
        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        // 3. 테이블에 정의된 칼럼 가져오기
        // ContactsContract.CommonDataKinds.Phone 이 경로에 상수로 칼럼이 정의
        String[] projection = { ContactsContract.CommonDataKinds.Phone.CONTACT_ID // 인덱스 값 -- 한 사람의 번호가 여러 개인다던지 하면 중복될 수 있다.
                , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                , ContactsContract.CommonDataKinds.Phone.NUMBER };
        // 4. ContentResolver로 쿼리를 날림 -> resolver 가 provider 에게 쿼리하겠다고 요청
        Cursor cursor = resolver.query(phoneUri, projection, null, null, null);

        // 5. 커서가 리턴된다. 반복문을 돌면서 cursor 에 담긴 데이터를 하나씩 추출
        if (cursor != null) {
            while(cursor.moveToNext()) {
                // 5.1 이름으로 인덱스를 찾아준다
                int idIndex = cursor.getColumnIndex(projection[0]); // 이름을 넣으면 그 칼럼을 가져와준다.
                int nameIndex = cursor.getColumnIndex(projection[1]);
                int numberIndex = cursor.getColumnIndex(projection[2]);
                // 5.2 해당 index 를 사용해서 실제 값을 가져온다.
                String id = cursor.getString(idIndex);
                String name = cursor.getString(nameIndex);
                String number = cursor.getString(numberIndex);

                Contact contact = new Contact();
                contact.setId(id);
                contact.setName(name);
                contact.setTelNum(number);
                datas.add(contact);
            }
        }
        // 데이터 계열은 반드시 닫아줘야 한다.
        cursor.close();
        Collections.sort(datas);
        ContactList.addAll(datas);
        mAdapter.notifyDataSetChanged();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                                startActivity(intent);
                                Toast.makeText(getActivity(), "연락처 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getActivity(), "연락처 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSION_STORAGE);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            getContacts(getActivity());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    getContacts(getActivity());
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


}