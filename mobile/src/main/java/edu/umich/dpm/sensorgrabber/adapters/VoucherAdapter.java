package edu.umich.dpm.sensorgrabber.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.umich.dpm.sensorgrabber.CouponDetails;
import edu.umich.dpm.sensorgrabber.R;
import edu.umich.dpm.sensorgrabber.fragments.TockonFragment;
import edu.umich.dpm.sensorgrabber.models.FetchVouchers;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.ViewHolderClass> {

    private Context context;

    List<FetchVouchers> fetchDataList;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference1;

    Bitmap bitmap;

    public VoucherAdapter(Context context, List<FetchVouchers> fetchDataList){
        this.context = context;
        this.fetchDataList = fetchDataList;
    }

    @NonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_item,parent,false);
        ViewHolderClass viewHolderClass= new ViewHolderClass(view);
        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderClass holder, int position) {
        ViewHolderClass viewHolderClass=(ViewHolderClass)holder;

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference1 = firebaseDatabase.getReference("vouchers");

        FetchVouchers fetchData = fetchDataList.get(position);

        String imgUrl = fetchData.getVoucherImg();

        Log.d("VOUCHER_IMG", "" + imgUrl);

        Picasso.with(context)
                .load(imgUrl)
                .into(viewHolderClass.imgVoucher);

        viewHolderClass.txtPoints.setText("" + fetchData.getPoints() + " pts");
        viewHolderClass.txtAED.setText("" + fetchData.getAed());

    }

    public void filterList(ArrayList<FetchVouchers> filteredList) {
        fetchDataList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return fetchDataList.size();
    }

    //MADE STATIC
    public class ViewHolderClass extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imgVoucher;
        TextView txtPoints, txtAED;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            imgVoucher = itemView.findViewById(R.id.imgVoucher);
            txtPoints = itemView.findViewById(R.id.txtPoints);
            txtAED = itemView.findViewById(R.id.txtAED);

            //txtViewOnMap.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("ItemClickViewHolder", "Clicked");

            int position = this.getAdapterPosition();
            FetchVouchers fetchData = fetchDataList.get(position);
            String imgUrl = fetchData.getVoucherImg();
            String points = String.valueOf(fetchData.getPoints());
            String aed = String.valueOf(fetchData.getAed());
            String brandName = String.valueOf(fetchData.getBrandName());

            int userPoints = TockonFragment.userPoints;

            Intent intent = new Intent(v.getContext(), CouponDetails.class);
            intent.putExtra("imgUrl", imgUrl);
            intent.putExtra("points", points);
            intent.putExtra("aed", aed);
            intent.putExtra("brandName", brandName);
            intent.putExtra("userPoints", userPoints);

            v.getContext().startActivity(intent);
        }
    }
}
