import pytest
from math_utils import MathUtils

@pytest.fixture
def sample_numbers():
    """Фикстура для передачи тестовых данных."""
    return [1, 2, 3, 4, 5, 6]

def test_factorial():
    assert MathUtils.factorial(0) == 1
    assert MathUtils.factorial(5) == 120
    assert MathUtils.factorial(7) == 5040
    with pytest.raises(ValueError):
        MathUtils.factorial(-1)

def test_fibonacci():
    assert MathUtils.fibonacci(0) == 0
    assert MathUtils.fibonacci(1) == 1
    assert MathUtils.fibonacci(10) == 55
    assert MathUtils.fibonacci(15) == 610
    with pytest.raises(ValueError):
        MathUtils.fibonacci(-5)

def test_gcd():
    assert MathUtils.gcd(48, 18) == 6
    assert MathUtils.gcd(7, 5) == 1
    assert MathUtils.gcd(-48, -18) == 6

def test_sqrt_newton():
    assert MathUtils.sqrt_newton(9) == 3.0
    assert MathUtils.sqrt_newton(25) == 5.0
    assert MathUtils.sqrt_newton(2) == 1.414214
    with pytest.raises(ValueError):
        MathUtils.sqrt_newton(-4)

def test_geometric_mean(sample_numbers):
    assert MathUtils.geometric_mean(sample_numbers) == 3.380015
    with pytest.raises(ValueError):
        MathUtils.geometric_mean([])

