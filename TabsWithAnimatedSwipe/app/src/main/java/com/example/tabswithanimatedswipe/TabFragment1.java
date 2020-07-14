package com.example.tabswithanimatedswipe;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
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
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

// Instances of this class are fragments representing a single
// object in our collection.
public class TabFragment1 extends Fragment implements TextWatcher {
    private static final int MY_PERMISSION_CONTACT = 1111;
    private static final int MY_PERMISSION_EXTERNAL_READ = 2222;
    private static final int REQUESTCODE_GALLERY = 111;

    RecyclerView recyclerView;
    EditText editTextSearchBox;
    MyAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    private List<Contact> ContactList;
    // Button button_contact;
    // Button button_clearing;
    ImageButton button_popup;
    FloatingActionButton fab_add;
    View fragmentView;
    private int targetPos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.new_fragment_tab1, container, false);
        button_popup = fragmentView.findViewById(R.id.imageButton_searchBox);
        button_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });
        /*
        button_contact = fragmentView.findViewById(R.id.get_contacts_button);
        button_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContacts();
            }
        });
        button_clearing = fragmentView.findViewById(R.id.clearing_button);
        button_clearing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactList.clear();
                mAdapter.notifyDataSetChanged();
            }
        });
        */
        fab_add = fragmentView.findViewById(R.id.addition_fab);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAdditionDialog();
            }
        });

        return fragmentView;
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        ContactList = new ArrayList<>();

        editTextSearchBox = view.findViewById(R.id.searchBox);
        editTextSearchBox.addTextChangedListener(this);
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
            private ImageButton imageButton;

            public MyViewHolder(View v) {
                super(v);

                name_view = v.findViewById(R.id.textView_name);
                telNum_view = v.findViewById(R.id.textView_telNum);
                imageButton = v.findViewById(R.id.imageButton_listitem);
                imageButton.setBackground(new ShapeDrawable(new OvalShape()));
                imageButton.setClipToOutline(true);
                imageButton.setOnClickListener(new View.OnClickListener() { // 프로필사진을 클릭했을 때 무슨 일을 할 지 정해주는 리스너 생성 후 연결
                    // 프로필사진을 클릭하면 무슨 일이 일어날 지 정하기
                    @Override
                    public void onClick(View view) {
                        int pos = getAdapterPosition(); // 몇 번째 아이템이었는지 위치 찾기
                        if (pos != RecyclerView.NO_POSITION) {
                            // TODO : use pos.
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // 아이템 클릭 했을 때 띄울 대화상자 만들기
                            builder.setItems(R.array.photo_selected_dialog, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    switch (i) {
                                        // 갤러리에서 불러오기 눌렀을 때
                                        case 0:
                                            targetPos = pos;
                                            checkExternalReadPermission();
                                            break;
                                        // 삭제하기 눌렀을 때
                                        case 1:
                                            ContactList.get(pos).setProfile(getResources().getDrawable(R.drawable.ic_baseline_account_circle_48));
                                            mAdapter.notifyDataSetChanged();
                                            break;
                                    }
                                }
                            }).show();
                        }
                    }
                });
                v.setOnClickListener(new View.OnClickListener() { // RecyclerView 내의 아이템을 클릭했을 때 무슨 일을 할 지 정해주는 리스너 생성 후 연결
                    @Override
                    public void onClick(View v) { // 아이템 클릭 할 때 무엇을 할 지 정하기; 여기 너무 더럽다
                        int pos = getAdapterPosition(); // 몇 번째 아이템이었는지 위치 찾기
                        if (pos != RecyclerView.NO_POSITION) {
                            // TODO : use pos.
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // 아이템 클릭 했을 때 띄울 대화상자 만들기
                            builder.setItems(R.array.item_selected_dialog, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    switch (i) {
                                        // 전화 걸기 버튼 눌렀을 때
                                        case 0:
                                            String telNum_dial = ContactList.get(pos).getTelNum();
                                            dialPhoneNumber(telNum_dial);
                                            break;
                                        // SMS 보내기 버튼 눌렀을 때
                                        case 1:
                                            String telNum_SMS = ContactList.get(pos).getTelNum();
                                            composeSMSMessage(telNum_SMS);
                                            break;
                                        // when clicked edit button; 편집 버튼 눌렀을 때
                                        case 2:
                                            createEditionDialog(pos);
                                            break;
                                        // when clicked delete button; 삭제하기 눌렀을 때
                                        case 3:
                                            ContactList.remove(pos); // 정렬된 리스트에서 삭제할 때에는 다시 정렬할 필요 없음
                                            mAdapter.notifyDataSetChanged();
                                            break;
                                    }
                                }
                            }).show();
                        }
                    }
                });
            }

            private void onBind(Contact contact) {
                name_view.setText(contact.getName());
                telNum_view.setText(contact.getTelNum());
                if(!contact.isProfileEmpty())
                    imageButton.setImageDrawable(contact.getProfile());
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
        checkContactPermission();
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
                contact.setProfile(getResources().getDrawable(R.drawable.ic_baseline_account_circle_48));
                datas.add(contact);
            }
        }
        // 데이터 계열은 반드시 닫아줘야 한다.
        cursor.close();
        for (Contact c : datas)
            if(!ContactList.contains(c))
                ContactList.add(c);
        Collections.sort(ContactList);
        mAdapter.notifyDataSetChanged();
    }

    private void checkContactPermission() {
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
                        MY_PERMISSION_CONTACT);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            getContacts(getActivity());
        }
    }

    private void checkExternalReadPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
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
                                Toast.makeText(getActivity(), "외부 저장소 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getActivity(), "외부 저장소 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSION_EXTERNAL_READ);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            Intent intent = new Intent();
            // 기기 기본 갤러리 접근 하는 Intent
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(intent, REQUESTCODE_GALLERY);
        }
    }

    private void createAdditionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialog_layout = inflater.inflate(R.layout.dialog_addcontact, null);
        builder.setView(dialog_layout)
        // Add action buttons
                .setPositiveButton(R.string.dialog_positive_button_string, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // add Contatct to ContactList
                        EditText editText_name = dialog_layout.findViewById(R.id.name);
                        EditText editText_phoneNum = dialog_layout.findViewById(R.id.phone_number);

                        String name = editText_name.getText().toString();
                        String phoneNum = editText_phoneNum.getText().toString();

                        if (name.isEmpty() || phoneNum.isEmpty() ) {
                            Toast.makeText(getActivity(), "정보를 모두 입력해 주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Contact contact = new Contact(name, phoneNum);
                        if (ContactList.contains(contact)) {
                            Toast.makeText(getActivity(), "이미 동일한 연락처가 존재합니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        contact.setProfile(getResources().getDrawable(R.drawable.ic_baseline_account_circle_48));
                        ContactList.add(contact);
                        Collections.sort(ContactList);
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.dialog_negative_button_string, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    private void createEditionDialog(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View dialog_layout = inflater.inflate(R.layout.dialog_editcontact, null);
        EditText editText_name = dialog_layout.findViewById(R.id.name);
        EditText editText_phoneNum = dialog_layout.findViewById(R.id.phone_number);
        editText_name.setText(ContactList.get(pos).getName());
        editText_phoneNum.setText(ContactList.get(pos).getTelNum());

        builder.setView(dialog_layout)
                // 확인 버튼 추가
                .setPositiveButton(R.string.edition_dialog_positive_button_string, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // edit Contatct on the position 'pos'
                        String name = editText_name.getText().toString();
                        String phoneNum = editText_phoneNum.getText().toString();

                        if (name.isEmpty() || phoneNum.isEmpty() ) {
                            Toast.makeText(getActivity(), "정보를 모두 입력해 주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Contact contact = ContactList.get(pos);
                        contact.setName(name);
                        contact.setTelNum(phoneNum);
                        if (ContactList.contains(contact)) {
                            Toast.makeText(getActivity(), "이미 동일한 연락처가 존재합니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Collections.sort(ContactList);
                        mAdapter.notifyDataSetChanged();
                    }
                })
                // 취소 버튼 추가
                .setNegativeButton(R.string.dialog_negative_button_string, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    // 현재 작동을 잘 못하고 있음. grantResults 배열이 어떤 식인지 잘 모르겠음.
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_CONTACT: {
                // If request is cancelled, the result arrays are empty.
                // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                if (grantResults.length > 0) {
                    boolean check_result = true;
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            check_result = false;
                            break;
                        }
                    }
                    if (check_result) {
                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.
                        getContacts(getActivity());
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            }

            case MY_PERMISSION_EXTERNAL_READ: {
                // If request is cancelled, the result arrays are empty.
                // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUESTCODE_GALLERY && resultCode == RESULT_OK) {
            try {
                InputStream is = getActivity().getContentResolver().openInputStream(data.getData());
                Bitmap bm = BitmapFactory.decodeStream(is);
                is.close();
                ContactList.get(targetPos).setProfile(new BitmapDrawable(bm));
                mAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(requestCode == REQUESTCODE_GALLERY && resultCode == RESULT_CANCELED) {
            Toast.makeText(getActivity(), "취소", Toast.LENGTH_SHORT).show();
        }
    }

    private void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void composeSMSMessage(String phoeNumber) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + phoeNumber));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.popup_menu_load:
                        setContacts();
                        return true;
                    case R.id.popup_menu_clear:
                        ContactList.clear();
                        mAdapter.notifyDataSetChanged();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }
}