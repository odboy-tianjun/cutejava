### 创建数据库和用户

```sql
use
cutejava
db.createUser({
    user: "cutejava",
    pwd: "123456",
    roles: [{
        role: "dbOwner",
        db: "cutejava"
    }]
})
```