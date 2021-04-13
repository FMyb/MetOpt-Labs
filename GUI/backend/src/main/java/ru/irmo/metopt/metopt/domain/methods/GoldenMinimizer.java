package ru.irmo.metopt.metopt.domain.methods;

import java.util.function.Function;

public class GoldenMinimizer extends Minimizer {
    private static final double TAU = (Math.sqrt(5) - 1) / 2;
    private double curA, curB;
    private double f1, f2;
    private double x1, x2;
    private boolean isX1Set, isX2Set;

    public GoldenMinimizer(Function<Double, Double> fun, double a, double b, double eps) {
        super(fun, a, b, eps);
        restart();
    }

    @Override
    public boolean hasNext() {
        return curB - curA > 2 * eps;
    }

    @Override
    protected Section nextIteration() {
        x1 = isX1Set ? x1 : curA + (1 - TAU) * (curB - curA);
        x2 = isX2Set ? x2 : curA + TAU * (curB - curA);
        f1 = isX1Set ? f1 : fun.apply(x1);
        f2 = isX2Set ? f2 : fun.apply(x2);
        if (f1 <= f2) {
            curB = x2;
            x2 = x1;
            f2 = f1;
            isX2Set = true;
            isX1Set = false;
        } else {
            curA = x1;
            x1 = x2;
            f1 = f2;
            isX1Set = true;
            isX2Set = false;
        }
        return new Section(curA, curB);
    }

    @Override
    public void restart() {
        curA = a;
        curB = b;
        f1 = f2 = 0;
        x1 = x2 = 0;
        isX1Set = isX2Set = false;
    }

    @Override
    public double getCurrentXMin() {
        return (curA + curB) / 2;
    }
}
