package tests;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

public final class OrderedRunner extends SpringJUnit4ClassRunner {

    public OrderedRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        final List<FrameworkMethod> list = super.computeTestMethods();
        final List<FrameworkMethod> copy = new ArrayList<>(list);
        copy.sort((f1, f2) -> {
                    final Order o1 = f1.getAnnotation(Order.class);
                    final Order o2 = f2.getAnnotation(Order.class);

                    return o1 == null || o2 == null ? -1 : o1.order() - o2.order();
                }
        );

        return copy;
    }
}