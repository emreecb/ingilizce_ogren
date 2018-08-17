package com.example.emirhan.iingilizce_ogren.abstraction.data;

import com.example.emirhan.iingilizce_ogren.abstraction.core.Chain;
import com.example.emirhan.iingilizce_ogren.abstraction.core.Ref;
import com.example.emirhan.iingilizce_ogren.abstraction.models.ScoreModel;
import com.example.emirhan.iingilizce_ogren.abstraction.models.UserModel;
import com.example.emirhan.iingilizce_ogren.abstraction.models.WordModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by emirhan on 12/19/2017.
 */

public class FirebaseService
        implements IWordService<Chain, WordModel>,
                   IScoreService<Chain, ScoreModel>,
                   IUserService<Task, UserModel> {

    private DatabaseReference getDbReference() {
        return FirebaseDatabase.getInstance().getReference();
    }

    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public boolean isAuthUser() {
        return getCurrentUser() == null;
    }

    public Task<AuthResult> loginUser(String email, String password) {

        return FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> loginAnonymous() {

        return FirebaseAuth.getInstance().signInAnonymously();
    }

    public void clearUser() {

        FirebaseAuth.getInstance().signOut();
    }

    public Task<AuthResult> registerUser(String email, String password) {

        if (getCurrentUser() != null) {

            AuthCredential credential = EmailAuthProvider.getCredential(email, password);
            return getCurrentUser().linkWithCredential(credential);

        } else {

            return FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password);
        }
    }

    public UserModel getAuthUser() {

        FirebaseUser user = getCurrentUser();

        if (user == null)
            return null;

        return new UserModel(
                user.getUid(),
                user.getDisplayName(),
                user.getEmail()
        );
    }

    public Task updateUser(UserModel newModel) {

        //@TODO su an sadece username guncelliyor
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setDisplayName(newModel.username)
                .build();

        return getCurrentUser().updateProfile(request);
    }

    private Chain changeWordScore(
            final int wordId,
            final boolean increaseCorrect,
            final Ref<ScoreModel> scoreOut) {

        final FirebaseUser user = getCurrentUser();
        final DatabaseReference ref = getDbReference()
            .child("users/"+user.getUid()+"/scores/"+Integer.toString(wordId));

        Chain task;

        task = Chain.runAsync(new Runnable() {
            @Override
            public void run() {

            final Object lock = new Object();

            ref.runTransaction(new Transaction.Handler() {

                public Transaction.Result doTransaction(MutableData mutableData) {

                    Integer correct   = mutableData.child("dogru").getValue(Integer.class);
                    Integer incorrect = mutableData.child("yanlis").getValue(Integer.class);

                    if (increaseCorrect) {

                        correct   = correct   == null ? 1 : ++correct;
                        incorrect = incorrect == null ? 0 : incorrect;

                    } else {

                        incorrect = incorrect == null ? 1 : ++incorrect;
                        correct   = correct   == null ? 0 : correct;
                    }

                    mutableData.child("dogru").setValue(correct);
                    mutableData.child("yanlis").setValue(incorrect);

                    return Transaction.success(mutableData);
                }

                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                    synchronized (lock) {

                        int correct   =  dataSnapshot.child("dogru").getValue(Integer.class);
                        int incorrect =  dataSnapshot.child("yanlis").getValue(Integer.class);

                        ScoreModel score = new ScoreModel(
                            wordId,
                            user.getUid(),
                            correct,
                            incorrect
                        );

                        scoreOut.set(score);

                        lock.notify();
                    }
                }
            });

            try {
                synchronized (lock) {

                    lock.wait();
                }

            } catch (InterruptedException e) {

            }
            }
        });

        return task;
    }

    public Chain increaseWordScore(int wordId, Ref<ScoreModel> out) {

        return changeWordScore(wordId, true, out);
    }

    public Chain decreaseWordScore(int wordId, Ref<ScoreModel> out) {

        return changeWordScore(wordId, false, out);
    }

    public Chain fetchRandomWord(final Ref<WordModel> out) {

        final DatabaseReference ref = getDbReference()
                .child("words");

        return Chain.runAsync(new Runnable() {
            @Override
            public void run() {

            final Object lock = new Object();

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    synchronized (lock) {

                        List<WordModel> words = new ArrayList<>();

                        for (DataSnapshot data: dataSnapshot.getChildren()) {

                            WordModel word = data.getValue(WordModel.class);
                            word.id = Integer.parseInt(data.getKey());

                            words.add(word);
                        }

                        WordModel chosen = words.get(new Random().nextInt(words.size()));
                        out.set(chosen);

                        lock.notify();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            try {
                synchronized (lock) {

                    lock.wait();
                }
            } catch (InterruptedException e) {

            }
            }
        });
    }

}
