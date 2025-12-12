Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/focal64"
  config.vm.hostname = "ci-vm"

  # Réseau
  config.vm.network "private_network", ip: "192.168.33.10"

  # Ressources (augmente si ton PC le permet)
  config.vm.provider "virtualbox" do |vb|
    vb.memory = 6144   # 6GB (mets 4096 si ton PC ne supporte pas)
    vb.cpus = 2
  end

  # Sync folder (par défaut OK)
  config.vm.synced_folder ".", "/vagrant"

  # (Optionnel) Provision de base
  config.vm.provision "shell", inline: <<-SHELL
    sudo apt-get update -y
    sudo apt-get install -y git curl
  SHELL
end
