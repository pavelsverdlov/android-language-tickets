package tickets.language.svp.languagetickets;

import android.content.Intent;
import android.os.Bundle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;

import tickets.language.svp.languagetickets.ui.viewModel.TicketViewModel;

/**
 * Created by Pasha on 1/17/2015.
 */
public final class Storage {
    //public final static String USER = "ru.alexanderklimov.myapp.USER";
 //   private final String storageKey = "storage";
    private final String selectedTicketKey = "selectedTicket";
    private final Bundle bundle;
//    private Intent intent;

    public Storage(Intent intent){
//        this.intent = intent;
        Bundle b = intent.getExtras();
//        Object d = null;
//        if(b != null) {
//            d = b.getSerializable(selectedTicketKey);
//        }
        this.bundle = b == null ? new Bundle() : b;
        if(b == null){
            putTo(intent);
        }
    }
//    public Storage(Bundle bundle){
//        Bundle b = bundle;//.getBundle(storageKey);
//        this.bundle = b == null ? new Bundle() : b;
//    }

    public TicketViewModel getSelectedTicket(){
//        Object t = intent.getParcelableExtra(selectedTicketKey);
        return (TicketViewModel)bundle.getSerializable(selectedTicketKey);
//        try {
//            return (TicketViewModel)bytes2Object(bundle.getByteArray(selectedTicketKey));
//        }catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        throw new InternalError("Selected ticket was not got.");
    }

    public void setSelectedTicket(TicketViewModel ticket){
        //intent.putExtra(selectedTicketKey, ticket);
        bundle.putSerializable(selectedTicketKey, ticket);
//        try {
        //    bundle.putByteArray(selectedTicketKey, object2Bytes(ticket));
//        } catch (IOException e) {
//            throw new InternalError("Selected ticket was not put.");
//        }
    }

    public void putTo(Intent intent){
        intent.putExtras(bundle);
        //intent.putExtra(selectedTicketKey,this.intent.getSerializableExtra(selectedTicketKey));
    }

    public Intent getNewIntent() {
        Intent intent = new Intent();
        putTo(intent);
        return intent;
    }

    static private byte[] object2Bytes( Object o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        return baos.toByteArray();
    }
    static private Object bytes2Object( byte[] raw ) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream( raw );
        ObjectInputStream ois = new ObjectInputStream( bais );
        Object o = ois.readObject();
        return o;
    }
}