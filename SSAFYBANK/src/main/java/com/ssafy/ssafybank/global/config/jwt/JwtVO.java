package com.ssafy.ssafybank.global.config.jwt;
/*
 * SECRET 노출되면 안된다. (클라우드AWS - 환경변수, 파일에 있는 것을 읽어서 써야 한다)
 * 리플래시 토큰 구현 안함
 */
public interface JwtVO {
    public static final String SECRET = "싸피뱅크싸피뱅크싸피뱅크싸피뱅크싸피뱅크싸피뱅크싸피뱅크싸피뱅크"; // HS256 (대칭키)
    //jwt토큰을 암호화해서 돌려줘야 함 서버만 알고있으면 되는 키 메타쾽(프론트는 키를 가지고 있을 필요가 없음)
    //노출시키면 안되니 db나 환경변수에서 끌어 옴
    public static final long EXPIRATION_TIME = 1000L * 60L * 60L * 24L * 150L;

    //한 시간 / 하루 ?
    public static final String TOKEN_PREFIX = "Bearer "; // 한칸 띄우기 주의하기
    public static final String HEADER = "Authorization"; //헤더이름

    public static final String REFRESH_HEADER = "RefreshToken"; //리프레시토큰 헤더 이름
}