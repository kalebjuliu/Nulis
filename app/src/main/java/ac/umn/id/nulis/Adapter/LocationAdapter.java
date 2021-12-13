package ac.umn.id.nulis.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ac.umn.id.nulis.HelperClass.Location;
import ac.umn.id.nulis.R;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    Context context;
    ArrayList<Location> list;
    private LocationAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(LocationAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    public LocationAdapter(Context context, ArrayList<Location> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LocationAdapter.LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.location_item, parent, false);
        return new LocationAdapter.LocationViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.LocationViewHolder  holder, int position) {
        Location location = list.get(position);
        holder.locationTitle.setText(location.getTitle());
        holder.locationDesc.setText(location.getDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder{
        TextView locationTitle, locationDesc;
        ImageView locationDelete;

        public LocationViewHolder(@NonNull View itemView, LocationAdapter.OnItemClickListener listener) {
            super(itemView);

            locationTitle = itemView.findViewById(R.id.tv_locationItem_title);
            locationDesc = itemView.findViewById(R.id.tv_locationItem_desc);
            locationDelete = itemView.findViewById(R.id.delete_location);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            locationDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

}
