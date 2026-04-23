# Sprint 3 - Prism Mock 使用说明（前端逻辑闭环）

目标：在后端未就绪时，基于 OpenAPI 契约用 Prism Mock 出可访问的接口，前端完成至少两个页面的逻辑闭环与边界测试。

## 1. OpenAPI 契约位置
- [openapi.yaml]

## 2. 启动 Prism（Docker Compose）

仓库已提供 [docker-compose.prism.yml]：

```bash
docker compose -f docker-compose.prism.yml up
```

启动后：
- Mock Server：`http://localhost:4010`
- POST `http://localhost:4010/api/game/command`
- GET  `http://localhost:4010/api/game/status`

## 3. 打开前端页面（两个核心页面）

前端页面在 [frontend]：
- [index.html]
- [command.html]
- [status.html]

推荐用本地静态服务器打开（避免浏览器跨域/文件协议限制），例如：

```bash
python -m http.server 8000
```

然后访问：
- `http://localhost:8000/frontend/index.html`

## 4. 边界测试建议（可截图提交）
- command 为空（应返回 4xx/错误示例）
- command 为未知值（例如 `dance`）
- status 正常拉取并展示 JSON

