// eslint-disable-next-line
declare const __brand: unique symbol;

/**
 * `Brand<B>`는 주어진 타입 `B`를 나타내는 고유한 브랜드를 생성합니다.
 *
 * @template B - 브랜드를 나타내는 타입
 */
type Brand<B> = { [__brand]: B };

/**
 * `Branded<T, B>`는 기본 타입 `T`에 브랜드 `B`를 추가한 타입을 생성합니다.
 * 브랜드는 타입에 대한 추가 식별자를 제공하여, 동일한 기본 타입을 가진 여러 타입을 구분하는 데 사용됩니다.
 *
 * @template T - 기본 타입
 * @template B - 브랜드를 나타내는 타입
 */
export type Branded<T, B> = T & Brand<B>;

/**
 * 사용 예시:
 *
 * ```typescript
 * type UserId = Branded<number, 'UserId'>;
 * type ProductId = Branded<number, 'ProductId'>;
 *
 * const userId: UserId = 123 as UserId;
 * const productId: ProductId = 123 as ProductId;
 *
 * // 서로 다른 브랜드를 가지므로, 컴파일 오류가 발생합니다.
 * // userId === productId;
 * ```
 *
 * 2024.08.07 작성자: 렛서
 */
