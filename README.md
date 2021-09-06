# 气温获取系统（DEMO）

## 基本需求

Please implement a method that can fetch and return the temperature of one certain county in China. Here are the specific features:
* Feature 1: The method signature is `public Optional<Integer> getTemperature(String province, String city, String county)`.
* Feature 2: If the location is invalid, return reasonable value.
* Feature 3: Add reasonable retry mechanism, cause there might be connection exception when calling the weather API.
* Feature 4: The method need block invocation if the TPS(transactions per second) is more than 100.

## 功能设计

**核心功能**
* 根据获取的省市区，分别调用省份\城市\地区Code查询接口
* 获取并拼装完整Code后，调用气温查询接口获取天气数据
* 根据返回的天气数据获取温度并返回

**缓存**
* 考虑中国省市区的变化极小，所以使用缓存缓存省市区及其对应的Code信息
   * 省市区缓存有效时间设置为1天
   * 如果在系统启动时一次性获取所有省市区信息并返回，按照中国目前的城市数，将会一次性调用几百次获取地区Code接口，这将大大增加HTTP请求失败的概率，所以考虑使用懒加载的方式，每次查询到当前城市才把当前城市的所有区域Code进行缓存。
* 实时气温也不是准实时数据，根据调查weather.com.cn页面实时气温数据为1小时更新一次，高德气温数据为每小时更新数次，所以当前区域气温数据缓存设置为20分钟更新一次

**限流**
* 需要对接口TPS进行限制，考虑可以进行灵活配置，所以使用Google Guava RateLimiter进行流量限值。是否开启限流，流量等待时间及TPS限制数量，在配置文件中进行配置（也可以方便的迁移到apollo等分布式配置中心中）

**重试机制**
* 考虑HTTP的不可靠性，在创建HTTP Client的时候需要配置相应重试机制，包括最大重试次数及重试时间间隔

**异常处理**
* 异常处理过程中，把功能性异常和非功能性异常分开。这样如果是使用在Web系统中，依然可以使用统一的异常处理，处理返回的不同异常类型
* 异常返回时都携带对应的Error Code，方便上层调用方获取异常后的判断及处理

## 技术使用
* Springboot
* SpringCache + ehCache（在对缓存灵活性有更高要求的系统或分布式系统中可以使用redis等）
* Google Guava RateLimiter
* OKHttp3
