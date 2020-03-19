import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Fb {
    public static void main(String[] args) {
        BCryptPasswordEncoder b=new BCryptPasswordEncoder();
        String lft = b.encode("lft");
        System.out.println(lft);

    }
}
