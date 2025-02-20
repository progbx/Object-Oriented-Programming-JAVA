package com.epam.rd.autotasks;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FactorialRegularInputTesting {
    Factorial factorial = new Factorial();
    public static Stream<Arguments> testCases() {
        return Stream.of(new String[] { "255",
                                "3350850684932979117652665123754814942022584063591740702576779884286208799035732771005626138126763314259280802118502282445926550135522251856727692533193070412811083330325659322041700029792166250734253390513754466045711240338462701034020262992581378423147276636643647155396305352541105541439434840109915068285430675068591638581980604162940383356586739198268782104924614076605793562865241982176207428620969776803149467431386807972438247689158656000000000000000000000000000000000000000000000000000000000000000" },
                        new String[] { "27", "10888869450418352160768000000" },
                        new String[] { "100",
                                "93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000" })
                .map(Arguments::of);
    }
    @ParameterizedTest
    @MethodSource("testCases")
    void testFactorial(String in, String expected) {
        assertEquals(expected, factorial.factorial(in));
    }
}