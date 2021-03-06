package app.pamelaiki.com;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class sMarketAdapter extends ArrayAdapter<sMarket>
{
    private Context sMarketcontext;
    private List<sMarket> BestMarketList = new ArrayList<>();

    public sMarketAdapter(@NonNull Context context, ArrayList<sMarket> list)
    {
        super(context, 0, list);
        sMarketcontext = context;
        BestMarketList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(sMarketcontext).inflate(R.layout.smarket_list_item,parent,false);

        sMarket currentsMarket =BestMarketList.get(position);

        TextView sMarketName = (TextView) listItem.findViewById(R.id.textView);
        sMarketName.setText(currentsMarket.getsMarketName());

        TextView sMarketDistance = (TextView) listItem.findViewById(R.id.textView2);
        sMarketDistance.setText(String.format("%.2f",currentsMarket.getsMarketDistance()) + " χλμ");

        return listItem;
    }
}
