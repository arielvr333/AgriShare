package com.example.agrishare.model;

public class Model {


    ModelFireBase modelFirebase = new ModelFireBase();
    public interface AddPostListener {
        void onComplete();
    }

    public void addPost(Post post) {
        modelFirebase.addPost(post);
    }

    public static final Model instance = new Model();
//
//    public Executor executor = Executors.newFixedThreadPool(1);
//    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
//

//    public enum StudentListLoadingState {
//        loading,
//        loaded
//    }
//
//    MutableLiveData<StudentListLoadingState> studentListLoadingState = new MutableLiveData<StudentListLoadingState>();
//
//    public LiveData<StudentListLoadingState> getStudentListLoadingState() {
//        return studentListLoadingState;
//    }

    private Model() {
      //  studentListLoadingState.setValue(StudentListLoadingState.loaded);
    }
}
