public class Account {
    @Id
    private int id;
    private String name;
    private double balance;

    // Constructors, getters, setters
}

Transaction.java:

@Entity
public class TransactionRecord {
    @Id
    @GeneratedValue
    private int id;
    private int fromAccountId;
    private int toAccountId;
    private double amount;

    // Constructors, getters, setters
}

AppConfig.java:

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "your.package")
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Bean
    public DataSource dataSource() { /* MySQL DataSource */ }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        // set Hibernate properties and annotated classes
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sf) {
        return new HibernateTransactionManager(sf);
    }
}

BankService.java:

@Service
public class BankService {
    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public void transfer(int fromId, int toId, double amount) {
        Session session = sessionFactory.getCurrentSession();
        Account from = session.get(Account.class, fromId);
        Account to = session.get(Account.class, toId);

        if (from.getBalance() < amount) throw new RuntimeException("Insufficient funds!");

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);

        session.save(new TransactionRecord(fromId, toId, amount));
    }
}

MainApp.java:

public class MainApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        BankService service = ctx.getBean(BankService.class);

        try {
            service.transfer(1, 2, 1000);
            System.out.println("Transaction Successful");
        } catch (Exception e) {
            System.out.println("Transaction Failed: " + e.getMessage());
        }
    }
}