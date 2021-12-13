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

import ac.umn.id.nulis.HelperClass.Chapter;
import ac.umn.id.nulis.R;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder>{
    Context context;
    ArrayList<Chapter> list;
    private ChapterAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onEditClick(int position);
    }
    public void setOnItemClickListener(ChapterAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    public ChapterAdapter(Context context, ArrayList<Chapter> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChapterAdapter.ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.chapter_item, parent, false);
        return new ChapterAdapter.ChapterViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterAdapter.ChapterViewHolder  holder, int position) {
        Chapter chapter = list.get(position);
        holder.chapterTitle.setText(chapter.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder{
        TextView chapterTitle;
        ImageView chapterDelete, chapterEdit;

        public ChapterViewHolder(@NonNull View itemView, ChapterAdapter.OnItemClickListener listener) {
            super(itemView);

            chapterTitle = itemView.findViewById(R.id.tv_chapterItem_title);
            chapterDelete = itemView.findViewById(R.id.delete_chapter);
            chapterEdit = itemView.findViewById(R.id.edit_chapter);


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

            chapterDelete.setOnClickListener(new View.OnClickListener() {
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

            chapterEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onEditClick(position);
                        }
                    }
                }
            });
        }
    }
}
