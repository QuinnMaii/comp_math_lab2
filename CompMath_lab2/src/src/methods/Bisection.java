package src.methods;

import src.objects.AnswerX;
import src.objects.Func;

//bisection method
public class Bisection {
    public AnswerX calculate (Func function, double precision, double begin, double end, int iterationCount) {

        double fl =function.calcFunc(begin);
        double fr = function.calcFunc(end);
        double m = (begin + end) / 2;
        double fm = function.calcFunc(m);
        if (Math.abs(fm) < precision || iterationCount >= 1e5)
            return new AnswerX( begin, iterationCount);
        if(fr * fm < 0) return calculate(function, precision, m, end, iterationCount + 1);
        else return calculate(function, precision, begin, m, iterationCount + 1);

    }
}

