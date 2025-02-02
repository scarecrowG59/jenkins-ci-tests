import math
from functools import lru_cache

class MathUtils:

    @staticmethod
    def factorial(n):
        """Вычисляет факториал числа рекурсивно."""
        if n < 0:
            raise ValueError("Factorial is not defined for negative numbers")
        if n == 0 or n == 1:
            return 1
        return n * MathUtils.factorial(n - 1)

    @staticmethod
    @lru_cache(maxsize=None)  # Оптимизация через кэширование
    def fibonacci(n):
        """Возвращает n-е число Фибоначчи."""
        if n < 0:
            raise ValueError("Fibonacci is not defined for negative numbers")
        if n == 0:
            return 0
        elif n == 1:
            return 1
        return MathUtils.fibonacci(n - 1) + MathUtils.fibonacci(n - 2)

    @staticmethod
    def gcd(a, b):
        """Вычисляет наибольший общий делитель (НОД) через алгоритм Евклида."""
        while b:
            a, b = b, a % b
        return abs(a)

    @staticmethod
    def sqrt_newton(n, tolerance=1e-10):
        """Вычисляет квадратный корень методом Ньютона."""
        if n < 0:
            raise ValueError("Cannot compute the square root of a negative number")
        x = n
        while True:
            root = 0.5 * (x + (n / x))
            if abs(root - x) < tolerance:
                return round(root, 6)
            x = root

    @staticmethod
    def geometric_mean(numbers):
        """Вычисляет среднее геометрическое списка чисел."""
        if not numbers:
            raise ValueError("Cannot compute geometric mean of an empty list")
        product = math.prod(numbers)
        return round(product ** (1 / len(numbers)), 6)

