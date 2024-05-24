import java.util.ArrayList;
import java.util.List;

class Main {
    public static void main(String[] args){
        BankSystem bankSystem = new BankSystem(new ArrayList<BankAccount>(), new ArrayList<Transaction>());
        Bank bank = new Bank(new ArrayList<BankBranch>(), bankSystem, 10000);

        BankBranch branch1 = bank.addBranch("420 spanish street", 1000);
        BankBranch branch2 = bank.addBranch("2020 dallas mavs", 1000);

        branch1.addTeller(new BankTeller(1));
        branch2.addTeller(new BankTeller(2));

        int personId1 = branch1.OpenAccount("Luka Doncic");
        int personId2 = branch2.OpenAccount("Kyrie Irving");

        branch1.deposit(personId1, 10000);
        branch2.deposit(personId2, 20000);

        branch1.withdraw(personId1, 30);

        bank.printTransaction();



    }
}

class Transaction {
    private int personId;
    private int tellerId;

    public Transaction(int personId, int tellerId){
        this.personId = personId;
        this.tellerId = tellerId;
    }

    public int getPersonId(){
        return personId;
    }
    public int getTellerId(){
        return tellerId;
    }
    public String getTransactionDes(){
        return "";
    }
}

class Deposit extends Transaction {
    private int amount;

    public Deposit(int personId, int tellerId, int amount){
        super(personId, tellerId);
        this.amount = amount;
    }
    @Override
    public String getTransactionDes(){
        return "Teller "+ getTellerId() + " deposited "+ amount + " to account number "+ getPersonId();
    }
}

class Withdraw extends Transaction {
    private int amount;

    public Withdraw(int personId, int tellerId, int amount) {
        super(personId, tellerId);
        this.amount = amount;
    }
    @Override
    public String getTransactionDes(){
        return "Teller "+ getTellerId() + " withdrew "+ amount + " from account number "+getPersonId();
    }
}

class OpenAccount extends Transaction {
    public OpenAccount(int personId, int tellerId){
        super(personId, tellerId);
    }
    @Override
    public String getTransactionDes(){
        return "Teller "+ getTellerId() + " opened account "+ getPersonId();
    }
}

class BankTeller {
    private int id;

    public BankTeller(int id){
        this.id = id;
    }
    public int getId(){
        return id; 
    }
}

class BankAccount {
    private int personId;
    private String name;
    private int balance;

    public BankAccount(int personId, String name, int balance){
        this.personId = personId;
        this.name = name;
        this.balance = balance;
    }
    public int getPersonId(){
        return personId;
    }
    public String getName(){
        return name;
    }

    public int getBalance(){
        return this.balance;
    }
    public void deposit(int amount){
        this.balance += amount;
    }
    public void withdraw(int amount){
        this.balance -= amount;
    }
}

class BankSystem {
    private List<BankAccount> accounts;
    private List<Transaction> transactions;

    public BankSystem(List<BankAccount> accounts, List<Transaction> transactions){
        this.accounts = accounts;
        this.transactions = transactions;
    }

    public BankAccount getAccount(int personId){
        return this.accounts.get(personId);
    }
    public List<BankAccount> geAccounts(){
        return this.accounts;
    }
    public List<Transaction> getTransactions(){
        return this.transactions;
    }
    public int openAccount(String personName, int tellerId){
        //Creating a new bank Account
        int personId = this.accounts.size();
        BankAccount account = new BankAccount(personId, personName, 0);
        this.accounts.add(account);

        //To log transcations
        Transaction transaction = new OpenAccount(personId, tellerId);
        this.transactions.add(transaction);
        return personId;
    }

    public void deposit(int personId, int tellerId, int amount){
        BankAccount account = this.getAccount(personId);
        account.deposit(amount);

        Transaction transaction = new Deposit(personId, tellerId, amount);
        this.transactions.add(transaction);
    }

    public void withdraw(int personId, int tellerId, int amount){
        if(amount > this.getAccount(personId).getBalance()){
            throw new Error("Insufficient funds in the account");
        }
        BankAccount account = this.getAccount(personId);
        account.withdraw(amount);

        Transaction transaction = new Withdraw(personId, tellerId, amount);
        this.transactions.add(transaction);
    }
}

class BankBranch {
    private String address;
    private int cashOnHand;
    private BankSystem bankSystem;
    private List<BankTeller> tellers;

    public BankBranch(String address, int cashOnHand, BankSystem bankSystem){
        this.address = address;
        this.cashOnHand = cashOnHand;
        this.bankSystem = bankSystem;
        this.tellers = new ArrayList<>();
    }

    public void addTeller(BankTeller teller){
        this.tellers.add(teller);
    }
    public BankTeller getOpenTeller(){
        int index = (int)Math.round(Math.random() * (this.tellers.size() - 1));
        return this.tellers.get(index);
    }
    public int OpenAccount(String personName){
        if(this.tellers.size() == 0){
            throw new Error("Please wait for tellers");
        }
        BankTeller teller = this.getOpenTeller();
        return this.bankSystem.openAccount(personName, teller.getId());
    }

    public void deposit(int personId, int amount){
        if(this.tellers.size() == 0){
            throw new Error("Please wait for tellers.");
        }
        this.cashOnHand += amount;
        BankTeller teller = this.getOpenTeller();
        this.bankSystem.deposit(personId, teller.getId(), amount);
    }

    public void withdraw(int personId, int amount){
        if(amount > this.cashOnHand){
            throw new Error("Not enough money in this branch at the moment.");
        }
        if(this.tellers.size() == 0){
            throw new Error("Please wait for tellers.");
        }

        this.cashOnHand -= amount;
        BankTeller teller = this.getOpenTeller();
        this.bankSystem.withdraw(personId, teller.getId(), amount);
    }

    public int collectCash(double ratio){
        int cashToCollect = (int)Math.round(this.cashOnHand * ratio);
        this.cashOnHand -= cashToCollect;
        return cashToCollect;
    }
    public void provideCash(int amount){
        this.cashOnHand += amount;
    }
}

class Bank {
    private List<BankBranch> branches;
    private BankSystem bankSystem;
    private int totalCash;

    public Bank(List<BankBranch> branches, BankSystem bankSystem, int totalCash){
        this.branches = branches;
        this.bankSystem = bankSystem;
        this.totalCash = totalCash;
    }
    public BankBranch addBranch(String address, int initialFunds){
        BankBranch branch = new BankBranch(address, initialFunds, this.bankSystem);
        this.branches.add(branch);
        return branch;
    }
    public void collectCash(double ration){
        for(BankBranch branch: this.branches){
            int cashCollected = branch.collectCash(ration);
            this.totalCash += cashCollected;
        }
    }
    public void printTransaction(){
        for(Transaction transaction: this.bankSystem.getTransactions()){
            System.out.println(transaction.getTransactionDes());
        }
    }
}


