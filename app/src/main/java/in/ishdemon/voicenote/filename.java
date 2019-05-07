package in.ishdemon.voicenote;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

public class filename extends AbstractItem<filename,filename.ViewHolder> {

    public String name;

    public filename(String name){
        this.name = name;

    }

    @NonNull
    @Override
    public filename.ViewHolder getViewHolder(View v) {
        return new filename.ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.parent_file;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.layout_file_item;
    }

    protected static class ViewHolder extends FastAdapter.ViewHolder<filename>{
        private TextView name;

        public ViewHolder(View view){
            super(view);
            this.name = view.findViewById(R.id.tv_name);
        }

        @Override
        public void bindView(filename item, List<Object> payloads) {
            name.setText(item.name);
        }

        @Override
        public void unbindView(filename item) {

        }
    }
}
