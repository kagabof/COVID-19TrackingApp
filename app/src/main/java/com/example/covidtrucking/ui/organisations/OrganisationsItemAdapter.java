package com.example.covidtrucking.ui.organisations;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidtrucking.OrganisationActivity;
import com.example.covidtrucking.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrganisationsItemAdapter extends RecyclerView.Adapter<OrganisationsItemAdapter.MyViewHolder> {
    Context context;
    int images[];
    String names[], locations[];
    List <OrganisationHelperClass> orgList;
    private AdapterView.OnItemClickListener mListener;

    public OrganisationsItemAdapter(Context ct, List<OrganisationHelperClass> orgList){
        context = ct;
        this.orgList = orgList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final OrganisationHelperClass org = orgList.get(position);
        holder.name.setText(org.getName());
        holder.loc.setText(org.getLocation());

        if(org.getLogoImageUri() == null) {
            holder.images.setImageResource(R.drawable.ic_baseline_image_24);
        } else {
            Picasso.with(context)
                    .load(org.getLogoImageUri())
                    .into(holder.images);
        }

        holder.images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrganisationActivity.class);

                intent.putExtra("org_name", org.getName());
                context.startActivity(intent);
                Toast.makeText(context, org.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orgList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView name, loc;
        ImageView images;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_orgName);
            loc = itemView.findViewById(R.id.tv_orgLocation);
            images = itemView.findViewById(R.id.img_id);

        }
    }
}
