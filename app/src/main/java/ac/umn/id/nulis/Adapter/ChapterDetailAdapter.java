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

import ac.umn.id.nulis.HelperClass.ChapterDetail;
import ac.umn.id.nulis.R;

public class ChapterDetailAdapter extends RecyclerView.Adapter<ChapterDetailAdapter.ChapterDetailViewHolder>{
    Context context;
    ArrayList<ChapterDetail> list;
    private ChapterDetailAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onEditClick(int position);
    }
    public void setOnItemClickListener(ChapterDetailAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    public ChapterDetailAdapter(Context context, ArrayList<ChapterDetail> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChapterDetailAdapter.ChapterDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.chapter_detail_item, parent, false);
        return new ChapterDetailAdapter.ChapterDetailViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterDetailAdapter.ChapterDetailViewHolder  holder, int position) {
        ChapterDetail chapterDetail = list.get(position);
        holder.chapterDetailTitle.setText(chapterDetail.getChapterDetailTitle());
        holder.chapterDetailContent.setText(chapterDetail.getChapterDetailContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ChapterDetailViewHolder extends RecyclerView.ViewHolder{
        TextView chapterDetailTitle;
        TextView chapterDetailContent;
        ImageView chapterDetailDelete, chapterDetailEdit;

        public ChapterDetailViewHolder(@NonNull View itemView, ChapterDetailAdapter.OnItemClickListener listener) {
            super(itemView);

            chapterDetailTitle = itemView.findViewById(R.id.tv_chapterDetailItem_title);
            chapterDetailContent = itemView.findViewById(R.id.tv_chapterDetailItem_content);
            chapterDetailDelete = itemView.findViewById(R.id.delete_chapter_detail);
            chapterDetailEdit = itemView.findViewById(R.id.edit_chapter_detail);


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

            chapterDetailDelete.setOnClickListener(new View.OnClickListener() {
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

            chapterDetailEdit.setOnClickListener(new View.OnClickListener() {
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