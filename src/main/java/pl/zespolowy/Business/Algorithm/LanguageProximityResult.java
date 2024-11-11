package pl.zespolowy.Business.Algorithm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.zespolowy.Language;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Getter
@Setter
@ToString(of = {"nameAbbreviation", "language1", "language2", "countedProximity", "numberOfWordsToNormalization"})
public class LanguageProximityResult {
    private final String nameAbbreviation;
    private final Language language1;
    private final Language language2;
    private AtomicInteger countedProximity;
    private AtomicInteger numberOfWordsToNormalization;
    private final Lock lock1 = new ReentrantLock();

    public LanguageProximityResult(Language language1, Language language2) {
        this.language1 = language1;
        this.language2 = language2;
        this.nameAbbreviation = setNameAbbreviation(language1, language2);
        countedProximity = new AtomicInteger();
        countedProximity.set(0);
        numberOfWordsToNormalization = new AtomicInteger();
        numberOfWordsToNormalization.set(0);
    }

    public String setNameAbbreviation(Language language1, Language language2) {
        return language1.getCode() + language2.getCode();
    }

    public void countedProximityAndNumberOfWordsToNormalizationIncrease(Integer proximity, Integer anotherNumber) {
        while (true) {
            if (!tryLock(lock1, 1000)) {
                sleep(100);
                continue;
            }
            countedProximity.addAndGet(proximity);
            numberOfWordsToNormalization.addAndGet(anotherNumber);

            lock1.unlock();
            break;
        }
    }

    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public boolean tryLock(Lock lock, long millis) {
        try {
            return lock.tryLock(millis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
