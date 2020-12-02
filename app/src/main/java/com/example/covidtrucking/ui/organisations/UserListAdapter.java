package com.example.covidtrucking.ui.organisations;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidtrucking.ProfileActivity;
import com.example.covidtrucking.R;
import com.example.covidtrucking.UserHelperClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {
    Context context;
    List<VisitOrgHelperClass> visitOrgList;

    public UserListAdapter(Context applicationContext, ArrayList<VisitOrgHelperClass> visitOrgList) {
        this.context = applicationContext;
        this.visitOrgList = visitOrgList;
    }

    @NonNull
    @Override
    public UserListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_item_layout, parent, false);
        return new UserListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.MyViewHolder holder, int position) {
        final VisitOrgHelperClass visit = visitOrgList.get(position);
        final UserHelperClass user = visit.getUser();
        holder.userFName.setText(user.getFirst_name()+" "+user.getOther_names());
        holder.userEmail.setText(user.getEmail());
        holder.timeIn.setText("In: "+visit.getVisitTime().toString());
        holder.timeOut.setText("Out: "+visit.getOutTime().toString());
//        holder.loc.setText(org.getLocation());

        if(user.getImage_url() == null) {
            holder.userImage.setImageResource(R.drawable.ic_baseline_image_24);
        } else {
            Picasso.with(context)
                    .load(user.getImage_url())
                    .into(holder.userImage);
        }

        if(visit.getIn()){
            holder.isInTrue.setVisibility(View.VISIBLE);
        } else {
            holder.isInFalse.setVisibility(View.VISIBLE);
        }

        holder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);

                intent.putExtra("user_id", user.getNational_id());
                context.startActivity(intent);
                Toast.makeText(context, user.getFirst_name(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return visitOrgList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView userFName, userOtherNames, timeIn, timeOut, userEmail;
        ImageView userImage, isInTrue, isInFalse, isInUnknown;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userFName = itemView.findViewById(R.id.tv_user_name_item);
            userImage = itemView.findViewById(R.id.img_user_profile_item);
            userEmail = itemView.findViewById(R.id.tv_user_email_item);
            timeIn = itemView.findViewById(R.id.tv_user_time_in_item);
            timeOut = itemView.findViewById(R.id.tv_user_time_out_item);
            isInTrue = itemView.findViewById(R.id.img_in);
            isInFalse = itemView.findViewById(R.id.img_out);

        }
    }
}
