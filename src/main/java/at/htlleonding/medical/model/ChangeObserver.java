package at.htlleonding.medical.model;
@FunctionalInterface
public interface ChangeObserver<T> {
    public void update(T observer);
}
