![StoneSDK](https://cloud.githubusercontent.com/assets/2567823/11539067/6300c838-990c-11e5-9831-4f8ce691859e.png)

# Stone HAL Demo
Este repositório visa auxiliar fabricantes de POS Android que desejam implementar a Hardware Abstraction Layer (HAL) da Stone, provendo uma suite de testes de validação da API.
A principio, à título de demonstração, a suite de testes utiliza o HAL Mock, uma biblioteca que tem o intuito de emular um fabricante utilizando arquivos json para mockar o comportamento da implementação.

Para executar a suite em um terminal real é necessário adicionar uma implementação no `build.gradle` do módulo `app`:

```groovy
// TODO change to implementation
implementation 'br.com.stone.posandroid:hal-mock:1.0.1'
```

E posteriormente ajustar o `HALConfig.kt` para utilizar o DeviceProvider correto:

```kotlin
// TODO change to your Provider
val deviceProvider: DeviceProvider = MockDependencyProvider()
```

Feitas estas modificações, é possível executar todos os testes da pasta `androidTest` para validar a implementação utilizada.

O Manual do Fabricante pode ser encontrado [aqui](https://hardware-stone-integration.readme.io/docs/)

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
