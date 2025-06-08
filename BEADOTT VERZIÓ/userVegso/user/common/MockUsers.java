package common;

public class MockUsers {

    /**
     * Creates a MockUser to mock StreamingUser for StreamingPlatform
     */
    public static void mockUsers(){
        for(int i = 0; i < 1000; i++){
            MockUser user = new MockUser();
            user.subscribe();
        }
    }

}
