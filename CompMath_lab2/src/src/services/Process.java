package src.services;

import org.jetbrains.annotations.NotNull;
import src.methods.Iteration;
import src.methods.Bisection;
import src.methods.Tangent;
import src.objects.AnswerX;
import src.objects.EqSystem;
import src.objects.Func;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Process {
    public Process() {
        startMessage();
        process();
        exitMessage();
    }

    private void process() {
        while (true) {
            String command = prompt();
            if (command.equals("q")) break;

            switch (command) {
                case "sing"     -> singular();
                case "syst"     -> multiple();
                case "h"        -> help();
                default         -> err();
            }
        }
    }

    private void startMessage() {
        System.out.println("""
                Laboratory №2: "Systems of non-linear equations"
                Option: 2ав
                Author: Mai Thi Le Quyen, P32302""");
        help();
    }

    private void exitMessage() {
        printLine();
    }

    private void printLine() {
        System.out.println();
    }

    private void help() {
        printLine();
        System.out.println("""
             List of available commands:
             sing - solve non-linear equation
             syst - solve a system of non-linear equations
             q - exit the program
             h - show a list of available commands""");
        printLine();
    }

    private void singular() {
        printLine();
        System.out.println("""
            Choose an equation:
            1: x - 1 - 0.5*sin(x) = 0
            2: x^3 - 2x^2 + 1 = 0
            3: ln(x) + x^3 - x = 0""");
        printLine();
        int eq_id = getId(3);
        System.out.println("Equation №" + eq_id);
        Func function = switch (eq_id) {
            case 1 -> new Func() {
                @Override
                public double calcFunc(double x) {
                    return x - 1 - 0.5* Math.sin(x);
                }
                @Override
                public double calcDer(double x) {
                    return 1 - Math.cos(x)/2;
                }
            };
            case 2 -> new Func() {
                @Override
                public double calcFunc(double x) {
                    return Math.pow(x, 3) - 2 * Math.pow(x, 2) + 1;
                }
                @Override
                public double calcDer(double x) {
                    return 3 * Math.pow(x, 2) - 4 * x;
                }
            };
            default -> new Func() {
                @Override
                public double calcFunc(double x) {
                    return Math.log10(x) + Math.pow(x, 3) - x ;
                }
                @Override
                public double calcDer(double x) {
                    return 3 * Math.pow(x, 2) + 1/x - 1 ;
                }
            };
        };

        double begin;
        double end;

        System.out.println("Enter the start value for the interval for the bisection method:");
        begin = getFloat();
        System.out.println("Enter the end value of the interval for the bisection method:");
        end = getFloat();

        System.out.println("Enter precision for bisection method:");
        double precision = getFloat();
        System.out.println("Enter the initial approximation for the tangent method:");
        double initApprox = getFloat();

        printLine();
        System.out.println("We carry out calculations using the bisection method ...");
        AnswerX secSolution = new Bisection().calculate(function, precision, begin, end, 1);
        if(secSolution.iterations >= 1e5) System.out.println("There is no solution");
        else  System.out.println("Answer: " + secSolution.toString());
        printLine();

        System.out.println("We carry out calculations using the tangent method..");
        AnswerX tangSolution = new Tangent().calculate(function, precision, initApprox, 1);
        if(tangSolution.iterations >= 1e5) System.out.println("There is no solution");
        else  System.out.println("Answer: " + tangSolution.toString());
        printLine();

    }

    private void multiple() {
        printLine();
        System.out.println("""
            Choose a system of equations:
            1: y = x^3;
               y = x^2 - 6.
               
            2: y = 0.1x^3;
               y = x^2 - 0.5.""");
        printLine();
        int eq_id = getId(2);
        System.out.println("System of equations №" + eq_id);

        EqSystem system;
        if (eq_id == 1) {
             system = new EqSystem() {
                @Override
                public double x1(double y) {
                    return Math.cbrt(y);
                }

                @Override
                public double y2(double x) {
                    return Math.pow(x, 2) - 6;
                }
            };
        } else {
            system = new EqSystem() {
                @Override
                public double x1(double y) {
                    return Math.cbrt(10 * y);
                }
                @Override
                public double y2(double x) {
                    return Math.pow(x, 2) - 0.5;
                }
            };
        }

        System.out.println("Enter precision:");
        double precision = getFloat();
        System.out.println("Enter initial approximation for x:");
        double x = getFloat();
        System.out.println("Enter an initial approximation for y:");
        double y = getFloat();

        printLine();
        System.out.println("Great! We carry out calculations by the Fixed-point iteration method ... ");
        System.out.println("System Solution: " +
                new Iteration().calculate(system, precision, x, y, 1).toString()
        );
    }

    private int getId (int upperLimit) {
        int ret = 0;
        while (true) {
            String temp = prompt();

            try { ret = Integer.parseInt(temp); }
            catch (NumberFormatException ignore) {}

            if (ret >= 1 && ret <= upperLimit) return ret;
            System.out.println("System Solution " + upperLimit);
        }
    }

    private double getFloat() {
        while (true) {
            String temp = prompt();

            try { return Double.parseDouble(temp); }
            catch (NumberFormatException e) { System.out.println("Invalid data, please try again"); }
        }
    }

    private void err() {
        System.out.println("There is no such command. Type \"h\" to see a list of available commands.");
    }

    private String prompt() {
        System.out.print(">");
        return readFromScanner(new Scanner(System.in));
    }

    private String readFromScanner (@NotNull Scanner scanner) {
        try { return scanner.nextLine(); }
        catch (NoSuchElementException e) { return "q"; }
    }
}
