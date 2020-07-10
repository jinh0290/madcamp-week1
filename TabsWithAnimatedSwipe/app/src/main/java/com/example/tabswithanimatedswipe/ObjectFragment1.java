package com.example.tabswithanimatedswipe;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Instances of this class are fragments representing a single
// object in our collection.
public class ObjectFragment1 extends Fragment implements TextWatcher {
    RecyclerView recyclerView;
    EditText editText;
    MyAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    private List<Contact> ContactList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_object1, container, false);
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        ContactList = getContacts(getActivity());

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

    public List<Contact> getContacts(Context context) {
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
        return datas;
    }

}