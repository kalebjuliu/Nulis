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

import ac.umn.id.nulis.HelperClass.Character;
import ac.umn.id.nulis.R;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>{
    Context context;
    ArrayList<Character> list;
    private CharacterAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(CharacterAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    public CharacterAdapter(Context context, ArrayList<Character> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CharacterAdapter.CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.character_item, parent, false);
        return new CharacterAdapter.CharacterViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterAdapter.CharacterViewHolder  holder, int position) {
        Character character = list.get(position);
        holder.charTitle.setText(character.getTitle());
        holder.charDesc.setText(character.getDescription());
        holder.charRole.setText(character.getRole());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CharacterViewHolder extends RecyclerView.ViewHolder{
        TextView charTitle, charDesc, charRole;
        ImageView characterDelete;

        public CharacterViewHolder(@NonNull View itemView, CharacterAdapter.OnItemClickListener listener) {
            super(itemView);

            charTitle = itemView.findViewById(R.id.tv_charItem_title);
            charDesc = itemView.findViewById(R.id.tv_charItem_desc);
            charRole = itemView.findViewById(R.id.tv_charItem_role);
            characterDelete = itemView.findViewById(R.id.delete_character);

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
            characterDelete.setOnClickListener(new View.OnClickListener() {
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
