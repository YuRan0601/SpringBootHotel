#雲澄旅館 後端專案
===
######雲城旅館 專案介紹
  
專案採用前後端分離的架構。  
前端部分使用Vue搭配Vite做為開發框架，並使用axios串接後端的RESTful API。  
後端部分基於Spring Boot框架，透過Spring Data JPA搭配Hibernate進行物件關係映射，並與Microsoft SQL Server資料庫進行資料存取。  
此外還有使用綠界科技完成測試付款功能。
  
網站的功能有 會員管理、訂房系統、伴手禮商城、周邊景點介紹、租車系統。
  
---
  
##Spring Boot專案
  
---
  
使用技術：
  
- Spring：用註解管理各種Bean(@Component、@RestController、@Service、@Repository)，並使用自動注入(@Autowired)創建Bean的物件。
- Spring MVC：在Controller類別上使用註解@RequestMapping以及方法上使用各種Mapping(@GetMapping、@PostMapping、@PutMapping、@DeleteMapping)來設定API的URL pattern。
- Hibernate：ORM框架，透過操作資料表對應的實體類來對資料庫裡的資料表進行增刪改查。
- Spring Data JPA：ORM框架，DAO Repository實現JpaRepository介面，並透過JpaRepository裡面定義的方法或在Repository介面裡使用JpaRepository的命名規則來自訂方法，操作資料表。
- Spring Mail：使用JavaMailSender傳送Email。
- lombok：在資料表對應的實體類或VO類別上使用註解，自動生成Constructor、Getter、Setter、hashcode、equals等方法。
